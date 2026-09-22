package com.cartit.service;

import com.cartit.dto.request.SendOtpRequest;
import com.cartit.dto.request.VerifyOtpRequest;
import com.cartit.dto.response.AuthResponse;

public interface AuthService {

    void sendOtp(SendOtpRequest request);

    AuthResponse verifyOtp(VerifyOtpRequest request);

    boolean checkPhone(String phone);

    boolean checkAdminPhone(String phone);
}
