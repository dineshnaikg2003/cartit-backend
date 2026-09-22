package com.cartit.security.otp;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.OtpVerification;
import com.cartit.enums.OtpPurpose;
import com.cartit.exception.InvalidOtpException;
import com.cartit.repository.OtpRepository;

@Service
@Transactional
public class OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;

    private final OtpRepository otpRepository;
    private final OtpSmsSender otpSmsSender;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(OtpRepository otpRepository, OtpSmsSender otpSmsSender) {
        this.otpRepository = otpRepository;
        this.otpSmsSender = otpSmsSender;
    }

    public void sendOtp(String phone) {


        String otp = generateOtp();

        // Remove any previous OTP for this phone
        OtpVerification otpVerification =
                otpRepository.findByPhone(phone)
                        .orElse(new OtpVerification());

        otpVerification.setPhone(phone);
        otpVerification.setPurpose(OtpPurpose.LOGIN);
        otpVerification.setPhone(phone);
        otpVerification.setOtp(otp);
        otpVerification.setAttempts(0);
        otpVerification.setVerified(false);
        otpVerification.setExpiresAt(
                LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        otpRepository.save(otpVerification);

        // Send OTP via configured SMS Provider (Fast2SMS, Twilio, 2Factor, or Console Fallback)
        otpSmsSender.sendOtp(phone, otp);
    }

    public boolean verifyOtp(String phone, String enteredOtp) {

        OtpVerification otpVerification = otpRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new InvalidOtpException("OTP not found."));

        if (otpVerification.getVerified()) {
            throw new InvalidOtpException("OTP already used.");
        }

        if (LocalDateTime.now().isAfter(otpVerification.getExpiresAt())) {

            otpRepository.delete(otpVerification);

            throw new InvalidOtpException("OTP has expired.");
        }

        if (otpVerification.getAttempts() >= MAX_ATTEMPTS) {

            otpRepository.delete(otpVerification);

            throw new InvalidOtpException("Maximum OTP attempts exceeded.");
        }

        if (!otpVerification.getOtp().equals(enteredOtp)) {

            otpVerification.setAttempts(
                    otpVerification.getAttempts() + 1);

            otpRepository.save(otpVerification);

            throw new InvalidOtpException("Invalid OTP.");
        }

        otpVerification.setVerified(true);

        otpRepository.save(otpVerification);

        // OTP is single-use
        otpRepository.delete(otpVerification);

        return true;
    }

    private String generateOtp() {

        return String.format(
                "%06d",
                secureRandom.nextInt(1_000_000));
    }
}
