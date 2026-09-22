package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.CreateOfferRequest;
import com.cartit.dto.request.UpdateOfferRequest;
import com.cartit.dto.response.OfferResponse;

public interface OfferService {

    OfferResponse createOffer(
            CreateOfferRequest request);

    List<OfferResponse> getAllOffers();

    OfferResponse getOffer(Long id);

    OfferResponse updateOffer(
            Long id,
            UpdateOfferRequest request);

    OfferResponse toggleOffer(Long id);

    List<OfferResponse> getActiveOffers();

    void deleteOffer(Long id);
}
