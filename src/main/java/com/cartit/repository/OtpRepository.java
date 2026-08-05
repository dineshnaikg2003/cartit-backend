package com.cartit.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.OtpVerification;
import com.cartit.enums.OtpPurpose;

public interface OtpRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByPhone(String phone);

    void deleteByPhone(String phone);
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
//    
//    Optional<OtpVerification> findByPhoneAndPurpose(
//            String phone,
//            OtpPurpose purpose);
//
//    void deleteByPhoneAndPurpose(
//            String phone,
//            OtpPurpose purpose);

}