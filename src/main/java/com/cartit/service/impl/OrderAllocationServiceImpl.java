package com.cartit.service.impl;

import org.springframework.stereotype.Service;
import com.cartit.service.OrderAllocationService;

@Service
public class OrderAllocationServiceImpl implements OrderAllocationService {

    private final com.cartit.repository.UserRepository userRepository;
    private final com.cartit.service.StoreService storeService;

    public OrderAllocationServiceImpl(com.cartit.repository.UserRepository userRepository,
                                       com.cartit.service.StoreService storeService) {
        this.userRepository = userRepository;
        this.storeService = storeService;
    }

    private static final double EARTH_RADIUS_KM = 6371.0;
    public static final double DEFAULT_MAX_STORE_RADIUS_KM = 5.0; // 5 km Dark Store Radius

    @Override
    public double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    @Override
    public boolean isWithinStoreRadius(double partnerLat, double partnerLng, double storeLat, double storeLng, double storeMaxRadiusKm) {
        double distance = calculateDistanceInKm(partnerLat, partnerLng, storeLat, storeLng);
        return distance <= storeMaxRadiusKm;
    }

    @Override
    public boolean isNearestPartnerAllocated(double partnerLat, double partnerLng, double storeLat, double storeLng, double maxRadiusKm) {
        return isWithinStoreRadius(partnerLat, partnerLng, storeLat, storeLng, maxRadiusKm);
    }

    @Override
    public com.cartit.entity.User allocateNearestDeliveryBoy(com.cartit.entity.Order order) {
        if (order == null) return null;

        com.cartit.entity.Store store = storeService.getStore();
        double storeLat = (store != null && store.getLatitude() != null) ? store.getLatitude() : 12.9352;
        double storeLng = (store != null && store.getLongitude() != null) ? store.getLongitude() : 77.6245;

        java.util.List<com.cartit.entity.User> deliveryBoys = userRepository.findByRoleAndActiveTrue(com.cartit.enums.Role.DELIVERY_BOY);

        if (deliveryBoys.isEmpty()) {
            return null;
        }

        // Find nearest available delivery boy
        com.cartit.entity.User nearestBoy = null;
        double minDistance = Double.MAX_VALUE;

        for (com.cartit.entity.User boy : deliveryBoys) {
            if (Boolean.TRUE.equals(boy.getIsAvailable()) && Boolean.TRUE.equals(boy.getIsOnline())) {
                double boyLat = boy.getLatitude() != null ? boy.getLatitude() : storeLat + 0.002;
                double boyLng = boy.getLongitude() != null ? boy.getLongitude() : storeLng + 0.002;

                double dist = calculateDistanceInKm(boyLat, boyLng, storeLat, storeLng);
                if (dist < minDistance) {
                    minDistance = dist;
                    nearestBoy = boy;
                }
            }
        }

        // Fallback: If no online/available delivery boy, pick any delivery boy
        if (nearestBoy == null && !deliveryBoys.isEmpty()) {
            nearestBoy = deliveryBoys.get(0);
        }

        if (nearestBoy != null) {
            order.setDeliveryBoy(nearestBoy);
            double boyLat = nearestBoy.getLatitude() != null ? nearestBoy.getLatitude() : storeLat + 0.002;
            double boyLng = nearestBoy.getLongitude() != null ? nearestBoy.getLongitude() : storeLng + 0.002;
            order.setCurrentDeliveryLatitude(boyLat);
            order.setCurrentDeliveryLongitude(boyLng);
            nearestBoy.setIsAvailable(false);
            userRepository.save(nearestBoy);
        }

        return nearestBoy;
    }
}
