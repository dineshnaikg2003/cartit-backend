package com.cartit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.OfferResponse;
import com.cartit.service.OfferService;

@RestController
@RequestMapping("/api/offers")
public class CustomerOfferController {

    private final OfferService offerService;

    public CustomerOfferController(
            OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<OfferResponse>>> getActiveOffers() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Active offers fetched successfully",
                        offerService.getActiveOffers()
                )
        );
    }
}