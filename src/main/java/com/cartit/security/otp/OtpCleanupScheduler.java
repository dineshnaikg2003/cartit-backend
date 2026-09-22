package com.cartit.security.otp;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.repository.OtpRepository;

@Component
public class OtpCleanupScheduler {

    private final OtpRepository otpRepository;

    public OtpCleanupScheduler(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void deleteExpiredOtps() {

        otpRepository.deleteByExpiresAtBefore(
                LocalDateTime.now());
    }
}