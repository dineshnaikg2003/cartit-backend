package com.cartit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.GoogleLoginRequest;
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
import com.cartit.security.oauth.GoogleTokenVerifierService;
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
    private final GoogleTokenVerifierService googleTokenVerifierService;

    public AuthServiceImpl(
            OtpService otpService,
            UserRepository userRepository,
            JwtService jwtService,
            UserResponseBuilder userResponseBuilder,
            GoogleTokenVerifierService googleTokenVerifierService) {

        this.otpService = otpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userResponseBuilder = userResponseBuilder;
        this.googleTokenVerifierService = googleTokenVerifierService;
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

            // Public customer registration always sets CUSTOMER role.
            user.setRole(Role.CUSTOMER);

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

    @Override
    public AuthResponse loginDeliveryWithGoogle(GoogleLoginRequest request) {
        if (request == null || request.getIdToken() == null || request.getIdToken().isBlank()) {
            throw new BadRequestException("idToken is required");
        }

        var payload = googleTokenVerifierService.verifyToken(request.getIdToken());
        String sub = payload.getSubject();
        String email = payload.getEmail();
        boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

        User user = null;

        // Case A: Existing delivery partner has googleSubject
        Optional<User> bySubject = userRepository.findByGoogleSubject(sub);
        if (bySubject.isPresent()) {
            user = bySubject.get();
            if (user.getRole() != Role.DELIVERY_BOY) {
                throw new UnauthorizedException("Your Google account is not linked to an authorized delivery partner account.");
            }
            if (!Boolean.TRUE.equals(user.getActive())) {
                throw new UnauthorizedException("Delivery partner account is inactive or suspended.");
            }
        } else if (email != null && !email.isBlank() && emailVerified) {
            // Case B: Existing DELIVERY_BOY has verified email matching Google email and googleSubject is empty -> safe 1-time link
            Optional<User> byEmail = userRepository.findByEmailIgnoreCase(email);
            if (byEmail.isPresent()) {
                User existing = byEmail.get();
                if (existing.getRole() != Role.DELIVERY_BOY) {
                    throw new UnauthorizedException("Your Google account is not linked to an authorized delivery partner account.");
                }
                if (!Boolean.TRUE.equals(existing.getActive())) {
                    throw new UnauthorizedException("Delivery partner account is inactive or suspended.");
                }
                if (existing.getGoogleSubject() == null || existing.getGoogleSubject().isBlank()) {
                    existing.setGoogleSubject(sub);
                    user = userRepository.save(existing);
                } else {
                    throw new UnauthorizedException("Your Google account is not linked to an authorized delivery partner account.");
                }
            }
        }

        // Case C: Unmatched account -> reject. NEVER auto-create DELIVERY_BOY.
        if (user == null) {
            throw new UnauthorizedException("Your Google account is not linked to an authorized delivery partner account.");
        }

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userResponseBuilder.build(user);

        return new AuthResponse(
                token,
                false,
                userResponse,
                user.getRole().name()
        );
    }

    @Override
    public AuthResponse loginDeliveryWithOtp(VerifyOtpRequest request) {
        if (request == null || request.getPhone() == null || request.getPhone().isBlank() || request.getOtp() == null || request.getOtp().isBlank()) {
            throw new BadRequestException("Phone and OTP are required");
        }

        otpService.verifyOtp(request.getPhone(), request.getOtp());

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UnauthorizedException("No delivery partner account found with this phone number."));

        if (user.getRole() != Role.DELIVERY_BOY) {
            throw new UnauthorizedException("This phone number is not registered as a delivery partner account.");
        }

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("Delivery partner account is inactive or suspended.");
        }

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userResponseBuilder.build(user);

        return new AuthResponse(
                token,
                false,
                userResponse,
                user.getRole().name()
        );
    }
}