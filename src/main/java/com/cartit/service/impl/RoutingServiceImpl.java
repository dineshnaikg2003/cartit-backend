package com.cartit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartit.dto.response.RouteResponse;
import com.cartit.service.RoutingService;

@Service
public class RoutingServiceImpl implements RoutingService {

    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public RouteResponse calculateRoute(Double originLat, Double originLng, Double destLat, Double destLng) {
        if (originLat == null || originLng == null || destLat == null || destLng == null) {
            RouteResponse err = new RouteResponse();
            err.setStatus("FAILED");
            err.setErrorMessage("Invalid origin or destination coordinates");
            return err;
        }

        // 1. Try Google Routes API if key configured
        if (googleMapsApiKey != null && !googleMapsApiKey.trim().isEmpty()) {
            try {
                RouteResponse googleRoute = callGoogleRoutesApi(originLat, originLng, destLat, destLng);
                if (googleRoute != null && "SUCCESS".equals(googleRoute.getStatus())) {
                    return googleRoute;
                }
            } catch (Exception e) {
                // Silently fallback to OSRM
            }
        }

        // 2. Fallback to OSRM Road Routing
        try {
            RouteResponse osrmRoute = callOsrmRoutingApi(originLat, originLng, destLat, destLng);
            if (osrmRoute != null && "SUCCESS".equals(osrmRoute.getStatus())) {
                return osrmRoute;
            }
        } catch (Exception e) {
            // Silently return unavailable status
        }

        // 3. No straight line fallback allowed per project requirements
        RouteResponse unavailable = new RouteResponse();
        unavailable.setPoints(new ArrayList<>());
        unavailable.setEncodedPolyline(null);
        unavailable.setDistanceKm(null);
        unavailable.setDurationMins(null);
        unavailable.setStatus("ROUTE_UNAVAILABLE");
        unavailable.setProvider("NONE");
        unavailable.setErrorMessage("No road routing provider available to calculate route");
        return unavailable;
    }

    @SuppressWarnings("unchecked")
    private RouteResponse callGoogleRoutesApi(Double originLat, Double originLng, Double destLat, Double destLng) {
        String url = "https://routes.googleapis.com/directions/v2:computeRoutes";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", googleMapsApiKey.trim());
        headers.set("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline");

        Map<String, Object> body = Map.of(
            "origin", Map.of("location", Map.of("latLng", Map.of("latitude", originLat, "longitude", originLng))),
            "destination", Map.of("location", Map.of("latLng", Map.of("latitude", destLat, "longitude", destLng))),
            "travelMode", "DRIVE",
            "routingPreference", "TRAFFIC_AWARE"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map resBody = response.getBody();
            List routes = (List) resBody.get("routes");
            if (routes != null && !routes.isEmpty()) {
                Map route0 = (Map) routes.get(0);
                Number distanceMeters = (Number) route0.get("distanceMeters");
                String durationStr = (String) route0.get("duration"); // e.g. "345s"
                Map polylineMap = (Map) route0.get("polyline");
                String encodedPolyline = polylineMap != null ? (String) polylineMap.get("encodedPolyline") : null;

                double distKm = distanceMeters != null ? distanceMeters.doubleValue() / 1000.0 : 0.0;
                double durMins = parseDurationSeconds(durationStr) / 60.0;
                List<double[]> points = encodedPolyline != null ? decodePolyline(encodedPolyline) : new ArrayList<>();

                return new RouteResponse(
                    encodedPolyline,
                    points,
                    Math.round(distKm * 10.0) / 10.0,
                    Math.round(durMins * 10.0) / 10.0,
                    "SUCCESS",
                    "GOOGLE_ROUTES_API"
                );
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private RouteResponse callOsrmRoutingApi(Double originLat, Double originLng, Double destLat, Double destLng) {
        String url = String.format(
            "http://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=polyline",
            originLng, originLat, destLng, destLat
        );

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map resBody = response.getBody();
            if ("Ok".equalsIgnoreCase((String) resBody.get("code"))) {
                List routes = (List) resBody.get("routes");
                if (routes != null && !routes.isEmpty()) {
                    Map route0 = (Map) routes.get(0);
                    String geometry = (String) route0.get("geometry");
                    Number distance = (Number) route0.get("distance");
                    Number duration = (Number) route0.get("duration");

                    double distKm = distance != null ? distance.doubleValue() / 1000.0 : 0.0;
                    double durMins = duration != null ? duration.doubleValue() / 60.0 : 0.0;
                    List<double[]> points = geometry != null ? decodePolyline(geometry) : new ArrayList<>();

                    return new RouteResponse(
                        geometry,
                        points,
                        Math.round(distKm * 10.0) / 10.0,
                        Math.round(durMins * 10.0) / 10.0,
                        "SUCCESS",
                        "OSRM_ROUTING_ENGINE"
                    );
                }
            }
        }
        return null;
    }

    private double parseDurationSeconds(String durationStr) {
        if (durationStr == null) return 0.0;
        try {
            String clean = durationStr.replace("s", "").trim();
            return Double.parseDouble(clean);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static List<double[]> decodePolyline(String encoded) {
        List<double[]> poly = new ArrayList<>();
        if (encoded == null || encoded.isEmpty()) return poly;

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new double[]{lat / 1E5, lng / 1E5});
        }
        return poly;
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
