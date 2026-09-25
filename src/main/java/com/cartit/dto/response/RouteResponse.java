package com.cartit.dto.response;

import java.util.List;

public class RouteResponse {
    private String encodedPolyline;
    private List<double[]> points;
    private Double distanceKm;
    private Double durationMins;
    private String status;
    private String provider;
    private String errorMessage;

    public RouteResponse() {
    }

    public RouteResponse(String encodedPolyline, List<double[]> points, Double distanceKm, Double durationMins, String status, String provider) {
        this.encodedPolyline = encodedPolyline;
        this.points = points;
        this.distanceKm = distanceKm;
        this.durationMins = durationMins;
        this.status = status;
        this.provider = provider;
    }

    public String getEncodedPolyline() {
        return encodedPolyline;
    }

    public void setEncodedPolyline(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }

    public List<double[]> getPoints() {
        return points;
    }

    public void setPoints(List<double[]> points) {
        this.points = points;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(Double durationMins) {
        this.durationMins = durationMins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
