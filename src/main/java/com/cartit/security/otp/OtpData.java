package com.cartit.security.otp;

import java.time.LocalDateTime;

public class OtpData {

    private final String otp;
    private final LocalDateTime expiresAt;

    public OtpData(String otp, LocalDateTime expiresAt) {
        this.otp = otp;
        this.expiresAt = expiresAt;
    }

    public String getOtp() {
        return otp;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}