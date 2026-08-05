package com.cartit.security.otp;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.OtpVerification;
import com.cartit.exception.InvalidOtpException;
import com.cartit.repository.OtpRepository;

@Service
@Transactional
public class OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;

    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public void sendOtp(String phone) {

        // Remove any previous OTP for this phone
        otpRepository.deleteByPhone(phone);

        String otp = generateOtp();

        OtpVerification otpVerification = new OtpVerification();

        otpVerification.setPhone(phone);
        otpVerification.setOtp(otp);
        otpVerification.setAttempts(0);
        otpVerification.setVerified(false);
        otpVerification.setExpiresAt(
                LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        otpRepository.save(otpVerification);

        // TODO: Replace with SMS provider
        System.out.println("--------------------------------");
        System.out.println("CartIT OTP");
        System.out.println("Phone : " + phone);
        System.out.println("OTP   : " + otp);
        System.out.println("--------------------------------");
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