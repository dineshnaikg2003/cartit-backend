package com.cartit.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.OtpVerification;
import com.cartit.enums.OtpPurpose;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

	Optional<OtpVerification> findByPhone(String phone);

	@Modifying
	@Transactional
	void deleteByPhone(String phone);

	@Modifying
	@Transactional
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