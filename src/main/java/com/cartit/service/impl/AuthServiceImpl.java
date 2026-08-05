package com.cartit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cartit.dto.request.SendOtpRequest;
import com.cartit.dto.request.VerifyOtpRequest;
import com.cartit.dto.response.AuthResponse;
import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;
import com.cartit.enums.Role;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.InvalidOtpException;
import com.cartit.repository.UserRepository;
import com.cartit.security.jwt.JwtService;
import com.cartit.security.otp.OtpService;
import com.cartit.service.AuthService;
import com.cartit.service.builder.UserResponseBuilder;

@Service
public class AuthServiceImpl implements AuthService {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserResponseBuilder userResponseBuilder;
    
    public AuthServiceImpl(
            OtpService otpService,
            UserRepository userRepository,
            JwtService jwtService,
            UserResponseBuilder userResponseBuilder) {

    	this.userResponseBuilder=userResponseBuilder;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void sendOtp(SendOtpRequest request) {

        otpService.sendOtp(request.getPhone());
    }

    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request) {

    	otpService.verifyOtp(
    	        request.getPhone(),
    	        request.getOtp()
    	);

        Optional<User> existingUser =
                userRepository.findByPhone(request.getPhone());

        User user;
        boolean newUser;

        if (existingUser.isPresent()) {

            // Existing customer → LOGIN
            user = existingUser.get();
            newUser = false;

        } else {

            // New customer → REGISTRATION

            if (request.getName() == null ||
                    request.getName().isBlank()) {

            	throw new BadRequestException("Name is required");
            }
            if (request.getEmail() != null
            		&& !request.getEmail().isBlank()
            		&& userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            	
            	throw new BadRequestException(
            			"Email is already registered.");
            }

            user = new User();

            user.setName(request.getName().trim());
            user.setPhone(request.getPhone());
            user.setRole(Role.CUSTOMER);
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail().trim());
            }

            user = userRepository.save(user);

            newUser = true;
        }

        
        String token = jwtService.generateToken(user);
        UserResponse userResponse = userResponseBuilder.build(user);

        return new AuthResponse(
                token,
                newUser,
                userResponse
        );
    }
}
