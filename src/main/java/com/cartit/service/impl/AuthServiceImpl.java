package com.cartit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.SendOtpRequest;
import com.cartit.dto.request.VerifyOtpRequest;
import com.cartit.dto.response.AuthResponse;
import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;
import com.cartit.enums.Role;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.UnauthorizedException;
import com.cartit.repository.UserRepository;
import com.cartit.security.jwt.JwtService;
import com.cartit.security.otp.OtpService;
import com.cartit.service.AuthService;
import com.cartit.service.builder.UserResponseBuilder;

@Service
@Transactional
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

        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userResponseBuilder = userResponseBuilder;
    }

    @Override
    public void sendOtp(
            SendOtpRequest request) {

        otpService.sendOtp(
                request.getPhone()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPhone(
            String phone) {

        return userRepository
                .findByPhone(phone)
                .isPresent();
    }

    @Override
    public AuthResponse verifyOtp(
            VerifyOtpRequest request) {

        // -----------------------------------------------------
        // 1. VERIFY OTP FIRST
        // -----------------------------------------------------
        otpService.verifyOtp(
                request.getPhone(),
                request.getOtp()
        );

        // -----------------------------------------------------
        // 2. FIND EXISTING USER
        // -----------------------------------------------------
        Optional<User> existingUser =
                userRepository.findByPhone(
                        request.getPhone()
                );

        User user;
        boolean newUser;

        if (existingUser.isPresent()) {

            // -------------------------------------------------
            // EXISTING USER
            //
            // IMPORTANT:
            // Role comes ONLY from the database.
            // Never from the OTP request.
            // -------------------------------------------------
            user = existingUser.get();
            newUser = false;

        } else {

            // -------------------------------------------------
            // NEW CUSTOMER
            // -------------------------------------------------

            if (request.getName() == null ||
                    request.getName().isBlank()) {

                throw new BadRequestException(
                        "Name is required"
                );
            }

            if (request.getEmail() != null
                    && !request.getEmail().isBlank()
                    && userRepository
                            .existsByEmailIgnoreCase(
                                    request.getEmail()
                            )) {

                throw new BadRequestException(
                        "Email is already registered."
                );
            }

            user = new User();

            user.setName(
                    request.getName().trim()
            );

            user.setPhone(
                    request.getPhone()
            );

            // NEVER accept ADMIN role from public registration.
            if (request.getRole() == Role.DELIVERY_BOY) {
                user.setRole(Role.DELIVERY_BOY);
            } else {
                user.setRole(Role.CUSTOMER);
            }

            if (request.getEmail() != null
                    && !request.getEmail().isBlank()) {

                user.setEmail(
                        request.getEmail().trim()
                );
            }

            user = userRepository.save(user);

            newUser = true;
        }

        // -----------------------------------------------------
        // 3. GENERATE JWT
        // -----------------------------------------------------
        String token =
                jwtService.generateToken(user);

        // -----------------------------------------------------
        // 4. BUILD USER RESPONSE
        // -----------------------------------------------------
        UserResponse userResponse =
                userResponseBuilder.build(user);

        // -----------------------------------------------------
        // 5. RETURN ROLE FROM DATABASE
        // -----------------------------------------------------
        return new AuthResponse(
                token,
                newUser,
                userResponse,
                user.getRole().name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAdminPhone(
            String phone) {

        Optional<User> existingUser =
                userRepository.findByPhone(phone);

        if (existingUser.isEmpty()) {
            throw new UnauthorizedException(
                    "Unauthorized"
            );
        }

        User user =
                existingUser.get();

        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException(
                    "Unauthorized"
            );
        }

        return true;
    }
}