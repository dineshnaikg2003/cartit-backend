package com.cartit.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.StoreRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.StoreResponse;
import com.cartit.service.StoreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/store")
public class AdminStoreController {

    private final StoreService storeService;

    public AdminStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreDetails() {
        StoreResponse response = storeService.getStoreDetails();
        return ResponseEntity.ok(ApiResponse.success("Store details fetched successfully.", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<StoreResponse>> updateStoreDetails(@Valid @RequestBody StoreRequest request) {
        StoreResponse response = storeService.updateStoreDetails(request);
        return ResponseEntity.ok(ApiResponse.success("Store details updated successfully.", response));
    }
}
