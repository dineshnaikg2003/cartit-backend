package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.AddressRequest;
import com.cartit.dto.response.AddressResponse;
import com.cartit.dto.response.ApiResponse;
import com.cartit.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.createAddress(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Address created successfully",
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses() {

        List<AddressResponse> response =
                addressService.getMyAddresses();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Addresses fetched successfully",
                        response));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(
            @PathVariable Long addressId) {

        AddressResponse response =
                addressService.getAddressById(addressId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address fetched successfully",
                        response));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.updateAddress(addressId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address updated successfully",
                        response));
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable Long addressId) {

        AddressResponse response =
                addressService.setDefaultAddress(addressId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Default address updated successfully",
                        response));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(
            @PathVariable Long addressId) {

        addressService.deleteAddress(addressId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address deleted successfully",
                        null));
    }
}