package com.cartit.service;

public interface OrderAllocationService {

    /**
     * Calculates distance between two GPS coordinates using Haversine formula (in km).
     */
    double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2);

    /**
     * Checks if a delivery partner is strictly within the store's maximum delivery radius boundary.
     */
    boolean isWithinStoreRadius(double partnerLat, double partnerLng, double storeLat, double storeLng, double storeMaxRadiusKm);

    /**
     * Finds nearest on-duty delivery partners relative to store location within maximum radius.
     */
    boolean isNearestPartnerAllocated(double partnerLat, double partnerLng, double storeLat, double storeLng, double maxRadiusKm);

    /**
     * Allocates the nearest available delivery partner to the specified order.
     */
    com.cartit.entity.User allocateNearestDeliveryBoy(com.cartit.entity.Order order);
}
