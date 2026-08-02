package com.cartit.security.otp;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    private final SecureRandom secureRandom = new SecureRandom();

    public void sendOtp(String phone) {

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        LocalDateTime expiresAt =
                LocalDateTime.now().plusMinutes(5);

        otpStorage.put(
                phone,
                new OtpData(otp, expiresAt)
        );

        // Temporary development SMS sender
        System.out.println(
                "CartIT OTP for " + phone + " : " + otp
        );
    }

    public boolean verifyOtp(String phone, String enteredOtp) {

        OtpData otpData = otpStorage.get(phone);

        if (otpData == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(otpData.getExpiresAt())) {
            otpStorage.remove(phone);
            return false;
        }

        if (!otpData.getOtp().equals(enteredOtp)) {
            return false;
        }

        // OTP can only be used once
        otpStorage.remove(phone);

        return true;
    }
}