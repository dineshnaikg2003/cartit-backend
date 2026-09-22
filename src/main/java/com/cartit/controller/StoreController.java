package com.cartit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.StoreResponse;
import com.cartit.service.StoreService;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreDetails() {
        StoreResponse response = storeService.getStoreDetails();
        return ResponseEntity.ok(ApiResponse.success("Store details fetched successfully.", response));
    }
}
