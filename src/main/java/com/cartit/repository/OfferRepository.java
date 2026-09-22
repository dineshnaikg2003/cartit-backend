package com.cartit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByActiveTrueAndStartDateLessThanEqualAndExpiryDateGreaterThanEqual(
            LocalDateTime startDate,
            LocalDateTime expiryDate
    );
}