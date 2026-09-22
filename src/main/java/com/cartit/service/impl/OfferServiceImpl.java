package com.cartit.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.CreateOfferRequest;
import com.cartit.dto.request.UpdateOfferRequest;
import com.cartit.dto.response.OfferResponse;
import com.cartit.entity.Offer;
import com.cartit.entity.Product;
import com.cartit.entity.ProductImage;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.OfferRepository;
import com.cartit.repository.ProductImageRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.service.OfferService;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public OfferServiceImpl(
            OfferRepository offerRepository,
            ProductRepository productRepository,
            ProductImageRepository productImageRepository) {

        this.offerRepository = offerRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public OfferResponse createOffer(
            CreateOfferRequest request) {

        validateDates(
                request.getStartDate(),
                request.getExpiryDate());

        Product rewardProduct =
                findProduct(request.getRewardProductId());

        Offer offer = new Offer();

        offer.setName(request.getName().trim());
        offer.setDescription(request.getDescription());
        offer.setMinimumPurchaseAmount(
                request.getMinimumPurchaseAmount());
        offer.setRewardProduct(rewardProduct);
        offer.setRewardQuantity(
                request.getRewardQuantity());
        offer.setStartDate(
                request.getStartDate());
        offer.setExpiryDate(
                request.getExpiryDate());
        offer.setActive(
                request.getActive() == null
                        || request.getActive());
        offer.setRewardPrice(
                request.getRewardPrice()
            );
        offer.setUnit(
                request.getUnit()
        );


        return toResponse(
                offerRepository.save(offer));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getAllOffers() {

        return offerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferResponse> getActiveOffers() {

        LocalDateTime now = LocalDateTime.now();

        return offerRepository
                .findByActiveTrueAndStartDateLessThanEqualAndExpiryDateGreaterThanEqual(
                        now,
                        now
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OfferResponse getOffer(Long id) {

        return toResponse(findOffer(id));
    }

    @Override
    public OfferResponse updateOffer(
            Long id,
            UpdateOfferRequest request) {

        Offer offer = findOffer(id);

        String name =
                request.getName() != null
                        ? request.getName().trim()
                        : offer.getName();

        BigDecimal minimumPurchaseAmount =
                request.getMinimumPurchaseAmount() != null
                        ? request.getMinimumPurchaseAmount()
                        : offer.getMinimumPurchaseAmount();

        LocalDateTime startDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : offer.getStartDate();

        LocalDateTime expiryDate =
                request.getExpiryDate() != null
                        ? request.getExpiryDate()
                        : offer.getExpiryDate();

        validateDates(startDate, expiryDate);

        offer.setName(name);

        if (request.getDescription() != null) {
            offer.setDescription(
                    request.getDescription());
        }

        offer.setMinimumPurchaseAmount(
                minimumPurchaseAmount);

        if (request.getRewardProductId() != null) {

            Product rewardProduct =
                    findProduct(
                            request.getRewardProductId());

            offer.setRewardProduct(
                    rewardProduct);
        }

        if (request.getRewardQuantity() != null) {
            offer.setRewardQuantity(
                    request.getRewardQuantity());
        }

        offer.setStartDate(startDate);
        offer.setExpiryDate(expiryDate);

        if (request.getActive() != null) {
            offer.setActive(
                    request.getActive());
        }


            if (request.getRewardPrice() != null) {
                offer.setRewardPrice(
                    request.getRewardPrice()
                );
            }

            if (request.getUnit() != null) {
                offer.setUnit(
                    request.getUnit()
                );
            }

        return toResponse(
                offerRepository.save(offer));
    }

    @Override
    public OfferResponse toggleOffer(Long id) {

        Offer offer = findOffer(id);

        offer.setActive(!offer.isActive());

        return toResponse(
                offerRepository.save(offer));
    }

    @Override
    public void deleteOffer(Long id) {

        Offer offer = findOffer(id);

        offerRepository.delete(offer);
    }

    private Product findProduct(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reward product not found with id: "
                                        + id));
    }

    private Offer findOffer(Long id) {

        return offerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Offer not found with id: "
                                        + id));
    }

    private void validateDates(
            LocalDateTime startDate,
            LocalDateTime expiryDate) {

        if (startDate == null
                || expiryDate == null) {

            throw new BadRequestException(
                    "Start date and expiry date are required.");
        }

        if (!expiryDate.isAfter(startDate)) {

            throw new BadRequestException(
                    "Expiry date must be after start date.");
        }
    }

    private OfferResponse toResponse(
            Offer offer) {

        OfferResponse response =
                new OfferResponse();

        response.setId(offer.getId());
        response.setName(offer.getName());
        response.setDescription(
                offer.getDescription());

        response.setMinimumPurchaseAmount(
                offer.getMinimumPurchaseAmount());

        response.setRewardProductId(
                offer.getRewardProduct().getId());

        response.setRewardProductName(
                offer.getRewardProduct().getName());

        String rewardProductImageUrl = productImageRepository
                .findByProductIdAndPrimaryImageTrueAndActiveTrue(offer.getRewardProduct().getId())
                .map(ProductImage::getImageUrl)
                .orElse(null);
        response.setRewardProductImageUrl(rewardProductImageUrl);

        response.setRewardQuantity(
                offer.getRewardQuantity());

        response.setStartDate(
                offer.getStartDate());

        response.setExpiryDate(
                offer.getExpiryDate());

        response.setActive(
                offer.isActive());

        response.setCreatedAt(
                offer.getCreatedAt());

        response.setUpdatedAt(
                offer.getUpdatedAt());

        response.setRewardPrice(
                offer.getRewardPrice()
            );
        response.setUnit(
                offer.getUnit()
            );

        return response;
    }
}
