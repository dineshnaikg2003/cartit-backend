package com.cartit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.CreateOfferRequest;
import com.cartit.dto.request.UpdateOfferRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.OfferResponse;
import com.cartit.service.OfferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(
            OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OfferResponse>> createOffer(
            @Valid @RequestBody CreateOfferRequest request) {

        OfferResponse offer =
                offerService.createOffer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Offer created successfully",
                                offer));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getAllOffers() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offers fetched successfully",
                        offerService.getAllOffers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OfferResponse>> getOffer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer fetched successfully",
                        offerService.getOffer(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OfferResponse>> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOfferRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer updated successfully",
                        offerService.updateOffer(id, request)));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<OfferResponse>> toggleOffer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer status updated successfully",
                        offerService.toggleOffer(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOffer(
            @PathVariable Long id) {

        offerService.deleteOffer(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Offer deleted successfully",
                        null));
    }
}