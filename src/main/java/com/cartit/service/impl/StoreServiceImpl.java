package com.cartit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.StoreRequest;
import com.cartit.dto.response.StoreResponse;
import com.cartit.entity.Store;
import com.cartit.repository.StoreRepository;
import com.cartit.service.StoreService;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Store getStore() {
        return storeRepository.findAll().stream().findFirst().orElseGet(() -> {
            Store defaultStore = new Store();
            defaultStore.setName("CartIT Central Hub");
            defaultStore.setAddressLine1("100 Feet Road, Indiranagar");
            defaultStore.setCity("Bengaluru");
            defaultStore.setState("Karnataka");
            defaultStore.setCountry("India");
            defaultStore.setPostalCode("560038");
            defaultStore.setPhoneNumber("9876543210");
            defaultStore.setLatitude(12.9716);
            defaultStore.setLongitude(77.5946);
            defaultStore.setMaxDeliveryRadiusKm(15.0);
            return storeRepository.save(defaultStore);
        });
    }

    @Override
    public StoreResponse getStoreDetails() {
        return toResponse(getStore());
    }

    @Override
    public StoreResponse updateStoreDetails(StoreRequest request) {
        Store store = getStore();
        store.setName(request.getName());
        store.setAddressLine1(request.getAddressLine1());
        store.setAddressLine2(request.getAddressLine2());
        store.setLandmark(request.getLandmark());
        store.setCity(request.getCity());
        store.setState(request.getState());
        store.setCountry(request.getCountry());
        store.setPostalCode(request.getPostalCode());
        store.setPhoneNumber(request.getPhoneNumber());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());
        store.setMaxDeliveryRadiusKm(request.getMaxDeliveryRadiusKm());

        Store saved = storeRepository.save(store);
        return toResponse(saved);
    }

    @Override
    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private StoreResponse toResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setName(store.getName());
        response.setAddressLine1(store.getAddressLine1());
        response.setAddressLine2(store.getAddressLine2());
        response.setLandmark(store.getLandmark());
        response.setCity(store.getCity());
        response.setState(store.getState());
        response.setCountry(store.getCountry());
        response.setPostalCode(store.getPostalCode());
        response.setPhoneNumber(store.getPhoneNumber());
        response.setLatitude(store.getLatitude());
        response.setLongitude(store.getLongitude());
        response.setMaxDeliveryRadiusKm(store.getMaxDeliveryRadiusKm());
        return response;
    }
}
