package com.cartit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cartit.dto.request.GoogleLoginRequest;
import com.cartit.dto.request.VerifyOtpRequest;
import com.cartit.dto.response.AuthResponse;
import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;
import com.cartit.enums.Role;
import com.cartit.exception.UnauthorizedException;
import com.cartit.repository.UserRepository;
import com.cartit.security.jwt.JwtService;
import com.cartit.security.oauth.GoogleTokenVerifierService;
import com.cartit.security.otp.OtpService;
import com.cartit.service.builder.UserResponseBuilder;
import com.cartit.service.impl.AuthServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

@ExtendWith(MockitoExtension.class)
class DeliveryAuthTest {

    @Mock
    private OtpService otpService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserResponseBuilder userResponseBuilder;

    @Mock
    private GoogleTokenVerifierService googleTokenVerifierService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                otpService,
                userRepository,
                jwtService,
                userResponseBuilder,
                googleTokenVerifierService
        );
    }

    @Test
    @DisplayName("Google Login: Succeeds for existing authorized DELIVERY_BOY with matching googleSubject")
    void testGoogleLoginSuccess_WithSubject() {
        GoogleLoginRequest request = new GoogleLoginRequest("valid_token");
        Payload payload = new Payload();
        payload.setSubject("google_sub_123");
        payload.setEmail("rider@cartit.in");
        payload.setEmailVerified(true);

        User deliveryBoy = new User();
        deliveryBoy.setId(1L);
        deliveryBoy.setPhone("9876543210");
        deliveryBoy.setRole(Role.DELIVERY_BOY);
        deliveryBoy.setGoogleSubject("google_sub_123");
        deliveryBoy.setActive(true);

        UserResponse ur1 = new UserResponse();
        ur1.setId(1L);
        ur1.setName("Rider");
        ur1.setEmail("rider@cartit.in");
        ur1.setPhone("9876543210");
        ur1.setRole(Role.DELIVERY_BOY);

        when(googleTokenVerifierService.verifyToken("valid_token")).thenReturn(payload);
        when(userRepository.findByGoogleSubject("google_sub_123")).thenReturn(Optional.of(deliveryBoy));
        when(jwtService.generateToken(deliveryBoy)).thenReturn("cartit_jwt_token");
        when(userResponseBuilder.build(deliveryBoy)).thenReturn(ur1);

        AuthResponse response = authService.loginDeliveryWithGoogle(request);

        assertNotNull(response);
        assertEquals("cartit_jwt_token", response.getToken());
        assertEquals("DELIVERY_BOY", response.getRole());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Google Login: Controlled one-time linking for existing DELIVERY_BOY matching verified email")
    void testGoogleLoginSuccess_OneTimeLinking() {
        GoogleLoginRequest request = new GoogleLoginRequest("valid_token");
        Payload payload = new Payload();
        payload.setSubject("google_sub_456");
        payload.setEmail("rider2@cartit.in");
        payload.setEmailVerified(true);

        User existingRider = new User();
        existingRider.setId(2L);
        existingRider.setPhone("9765432109");
        existingRider.setEmail("rider2@cartit.in");
        existingRider.setRole(Role.DELIVERY_BOY);
        existingRider.setActive(true);
        existingRider.setGoogleSubject(null);

        UserResponse ur2 = new UserResponse();
        ur2.setId(2L);
        ur2.setName("Rider 2");
        ur2.setEmail("rider2@cartit.in");
        ur2.setPhone("9765432109");
        ur2.setRole(Role.DELIVERY_BOY);

        when(googleTokenVerifierService.verifyToken("valid_token")).thenReturn(payload);
        when(userRepository.findByGoogleSubject("google_sub_456")).thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("rider2@cartit.in")).thenReturn(Optional.of(existingRider));
        when(userRepository.save(existingRider)).thenReturn(existingRider);
        when(jwtService.generateToken(existingRider)).thenReturn("cartit_jwt_token_2");
        when(userResponseBuilder.build(existingRider)).thenReturn(ur2);

        AuthResponse response = authService.loginDeliveryWithGoogle(request);

        assertNotNull(response);
        assertEquals("google_sub_456", existingRider.getGoogleSubject());
        verify(userRepository).save(existingRider);
    }

    @Test
    @DisplayName("Google Login: Rejects unknown Google account without auto-creating DELIVERY_BOY")
    void testGoogleLoginRejects_UnknownAccount() {
        GoogleLoginRequest request = new GoogleLoginRequest("unknown_token");
        Payload payload = new Payload();
        payload.setSubject("google_sub_999");
        payload.setEmail("unknown@gmail.com");
        payload.setEmailVerified(true);

        when(googleTokenVerifierService.verifyToken("unknown_token")).thenReturn(payload);
        when(userRepository.findByGoogleSubject("google_sub_999")).thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("unknown@gmail.com")).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                authService.loginDeliveryWithGoogle(request)
        );

        assertTrue(ex.getMessage().contains("not linked to an authorized delivery partner account"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Google Login: Rejects CUSTOMER account attempting delivery login")
    void testGoogleLoginRejects_CustomerAccount() {
        GoogleLoginRequest request = new GoogleLoginRequest("customer_token");
        Payload payload = new Payload();
        payload.setSubject("google_sub_cust");
        payload.setEmail("customer@gmail.com");
        payload.setEmailVerified(true);

        User customer = new User();
        customer.setId(5L);
        customer.setRole(Role.CUSTOMER);
        customer.setGoogleSubject("google_sub_cust");
        customer.setActive(true);

        when(googleTokenVerifierService.verifyToken("customer_token")).thenReturn(payload);
        when(userRepository.findByGoogleSubject("google_sub_cust")).thenReturn(Optional.of(customer));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                authService.loginDeliveryWithGoogle(request)
        );

        assertTrue(ex.getMessage().contains("not linked to an authorized delivery partner account"));
    }

    @Test
    @DisplayName("OTP Login: Succeeds for authorized DELIVERY_BOY")
    void testDeliveryOtpLogin_Success() {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhone("9876543210");
        request.setOtp("654321");

        User rider = new User();
        rider.setId(1L);
        rider.setPhone("9876543210");
        rider.setRole(Role.DELIVERY_BOY);
        rider.setActive(true);

        UserResponse ur = new UserResponse();
        ur.setId(1L);
        ur.setName("Rider");
        ur.setEmail("rider@cartit.in");
        ur.setPhone("9876543210");
        ur.setRole(Role.DELIVERY_BOY);

        when(otpService.verifyOtp("9876543210", "654321")).thenReturn(true);
        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.of(rider));
        when(jwtService.generateToken(rider)).thenReturn("jwt_otp_token");
        when(userResponseBuilder.build(rider)).thenReturn(ur);

        AuthResponse response = authService.loginDeliveryWithOtp(request);

        assertNotNull(response);
        assertEquals("jwt_otp_token", response.getToken());
        assertEquals("DELIVERY_BOY", response.getRole());
    }

    @Test
    @DisplayName("OTP Login: Rejects CUSTOMER attempting delivery login via OTP")
    void testDeliveryOtpLogin_RejectsCustomer() {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhone("9111122222");
        request.setOtp("654321");

        User customer = new User();
        customer.setId(10L);
        customer.setPhone("9111122222");
        customer.setRole(Role.CUSTOMER);
        customer.setActive(true);

        when(otpService.verifyOtp("9111122222", "654321")).thenReturn(true);
        when(userRepository.findByPhone("9111122222")).thenReturn(Optional.of(customer));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                authService.loginDeliveryWithOtp(request)
        );

        assertTrue(ex.getMessage().contains("not registered as a delivery partner account"));
    }

    @Test
    @DisplayName("OTP Login: Rejects inactive or suspended delivery partner")
    void testDeliveryOtpLogin_RejectsInactive() {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhone("9876543210");
        request.setOtp("654321");

        User suspendedRider = new User();
        suspendedRider.setId(1L);
        suspendedRider.setPhone("9876543210");
        suspendedRider.setRole(Role.DELIVERY_BOY);
        suspendedRider.setActive(false);

        when(otpService.verifyOtp("9876543210", "654321")).thenReturn(true);
        when(userRepository.findByPhone("9876543210")).thenReturn(Optional.of(suspendedRider));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                authService.loginDeliveryWithOtp(request)
        );

        assertTrue(ex.getMessage().contains("inactive or suspended"));
    }
}
