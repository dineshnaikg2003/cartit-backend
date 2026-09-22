package com.cartit.security.otp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class OtpSmsSenderTest {

    @Test
    void testSendOtpConsoleFallback() {
        OtpSmsSenderImpl sender = new OtpSmsSenderImpl();
        ReflectionTestUtils.setField(sender, "enabled", true);
        ReflectionTestUtils.setField(sender, "provider", "console");

        assertDoesNotThrow(() -> sender.sendOtp("9876543210", "123456"));
    }

    @Test
    void testSendOtpFast2SmsWithoutKeyFallsBackSafely() {
        OtpSmsSenderImpl sender = new OtpSmsSenderImpl();
        ReflectionTestUtils.setField(sender, "enabled", true);
        ReflectionTestUtils.setField(sender, "provider", "fast2sms");
        ReflectionTestUtils.setField(sender, "fast2smsApiKey", "");

        assertDoesNotThrow(() -> sender.sendOtp("+919876543210", "654321"));
    }

    @Test
    void testSendOtpTwilioWithoutKeyFallsBackSafely() {
        OtpSmsSenderImpl sender = new OtpSmsSenderImpl();
        ReflectionTestUtils.setField(sender, "enabled", true);
        ReflectionTestUtils.setField(sender, "provider", "twilio");
        ReflectionTestUtils.setField(sender, "twilioAccountSid", "");
        ReflectionTestUtils.setField(sender, "twilioAuthToken", "");

        assertDoesNotThrow(() -> sender.sendOtp("9876543210", "888999"));
    }

    @Test
    void testSendOtpDisabledGraceful() {
        OtpSmsSenderImpl sender = new OtpSmsSenderImpl();
        ReflectionTestUtils.setField(sender, "enabled", false);

        assertDoesNotThrow(() -> sender.sendOtp("9876543210", "111222"));
    }
}
