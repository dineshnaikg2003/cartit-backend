package com.cartit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.SendOtpRequest;
import com.cartit.dto.request.VerifyOtpRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.AuthResponse;
import com.cartit.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkPhone(
            @Valid @RequestBody SendOtpRequest request) {

        boolean exists =
                authService.checkPhone(
                        request.getPhone());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Phone checked successfully",
                        exists));
    }

    @PostMapping("/admin/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> checkAdminPhone(
            @Valid @RequestBody SendOtpRequest request) {

        boolean authorized =
                authService.checkAdminPhone(
                        request.getPhone()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Admin verified successfully",
                        authorized
                )
        );
    }



    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        authService.sendOtp(request);

        return ResponseEntity.ok(
	    new ApiResponse<>(
	        true,
	        "OTP sent successfully",
	        null
	    )
	);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

	return ResponseEntity.ok(
		    new ApiResponse<>(
		        true,
		        "OTP verified successfully",
		        authService.verifyOtp(request)
		    )
		);
    }
}
