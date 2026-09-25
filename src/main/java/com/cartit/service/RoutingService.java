package com.cartit.service;

import com.cartit.dto.response.RouteResponse;

public interface RoutingService {
    RouteResponse calculateRoute(Double originLat, Double originLng, Double destLat, Double destLng);
}
