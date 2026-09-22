package com.cartit.security.otp;

public interface OtpSmsSender {

    void sendOtp(
            String phone,
            String otp);
}