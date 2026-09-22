package com.cartit.service;

import com.cartit.dto.request.StoreRequest;
import com.cartit.dto.response.StoreResponse;
import com.cartit.entity.Store;

public interface StoreService {
    StoreResponse getStoreDetails();
    StoreResponse updateStoreDetails(StoreRequest request);
    Store getStore();
    double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2);
}
