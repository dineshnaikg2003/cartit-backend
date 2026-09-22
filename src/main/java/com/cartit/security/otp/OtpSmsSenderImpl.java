package com.cartit.security.otp;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Production SMS sender implementation supporting multiple SMS providers:
 * - Fast2SMS (Indian SMS gateway - Quick OTP route)
 * - Twilio (Global SMS gateway)
 * - 2Factor (Indian transactional OTP gateway)
 * - Console Simulator (Safe fallback for local dev/testing)
 */
@Service
public class OtpSmsSenderImpl implements OtpSmsSender {

    private static final Logger log = LoggerFactory.getLogger(OtpSmsSenderImpl.class);

    private final HttpClient httpClient;

    @Value("${sms.enabled:true}")
    private boolean enabled;

    @Value("${sms.provider:fast2sms}")
    private String provider;

    // Fast2SMS configuration
    @Value("${sms.fast2sms.api-key:}")
    private String fast2smsApiKey;

    @Value("${sms.fast2sms.route:otp}")
    private String fast2smsRoute;

    // Twilio configuration
    @Value("${sms.twilio.account-sid:}")
    private String twilioAccountSid;

    @Value("${sms.twilio.auth-token:}")
    private String twilioAuthToken;

    @Value("${sms.twilio.from-number:}")
    private String twilioFromNumber;

    // 2Factor configuration
    @Value("${sms.twofactor.api-key:}")
    private String twoFactorApiKey;

    public OtpSmsSenderImpl() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public void sendOtp(String phone, String otp) {
        String cleanPhone = normalizeIndianPhone(phone);

        if (!enabled) {
            logSimulatedOtp(cleanPhone, otp, "SMS sending is disabled via config (sms.enabled=false)");
            return;
        }

        try {
            switch (provider.toLowerCase().trim()) {
                case "fast2sms":
                    sendFast2Sms(cleanPhone, otp);
                    break;
                case "twilio":
                    sendTwilioSms(cleanPhone, otp);
                    break;
                case "2factor":
                case "twofactor":
                    send2FactorSms(cleanPhone, otp);
                    break;
                case "console":
                default:
                    logSimulatedOtp(cleanPhone, otp, "Console provider selected");
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to send OTP SMS to {}: {}", cleanPhone, e.getMessage(), e);
            // Also log to console so admin/dev can still see the OTP in development
            logSimulatedOtp(cleanPhone, otp, "Fallback due to SMS dispatch failure");
        }
    }

    /**
     * Dispatches OTP using Fast2SMS Bulk V2 OTP Route
     */
    private void sendFast2Sms(String phone, String otp) throws Exception {
        if (fast2smsApiKey == null || fast2smsApiKey.isBlank() || fast2smsApiKey.startsWith("YOUR_")) {
            logSimulatedOtp(phone, otp, "Fast2SMS API Key not configured in application.properties (sms.fast2sms.api-key)");
            return;
        }

        String jsonPayload = String.format(
                "{\"route\":\"%s\",\"variables_values\":\"%s\",\"numbers\":\"%s\"}",
                fast2smsRoute,
                otp,
                phone
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.fast2sms.com/dev/bulkV2"))
                .header("authorization", fast2smsApiKey.trim())
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            log.info("Fast2SMS OTP sent successfully to {}. Response: {}", phone, response.body());
        } else {
            log.warn("Fast2SMS returned non-200 status {}: {}", response.statusCode(), response.body());
            logSimulatedOtp(phone, otp, "Fast2SMS error status: " + response.statusCode());
        }
    }

    /**
     * Dispatches OTP using Twilio Messages REST API
     */
    private void sendTwilioSms(String phone, String otp) throws Exception {
        if (twilioAccountSid == null || twilioAccountSid.isBlank() || twilioAccountSid.startsWith("YOUR_")
                || twilioAuthToken == null || twilioAuthToken.isBlank()) {
            logSimulatedOtp(phone, otp, "Twilio credentials not configured in application.properties");
            return;
        }

        String e164Phone = phone.startsWith("+") ? phone : "+91" + phone;
        String messageBody = "Your CartIT verification OTP is " + otp + ". Valid for 5 minutes. Do not share with anyone.";

        String form = "To=" + URLEncoder.encode(e164Phone, StandardCharsets.UTF_8)
                + "&From=" + URLEncoder.encode(twilioFromNumber.trim(), StandardCharsets.UTF_8)
                + "&Body=" + URLEncoder.encode(messageBody, StandardCharsets.UTF_8);

        String basicAuth = Base64.getEncoder().encodeToString(
                (twilioAccountSid.trim() + ":" + twilioAuthToken.trim()).getBytes(StandardCharsets.UTF_8)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.twilio.com/2010-04-01/Accounts/" + twilioAccountSid.trim() + "/Messages.json"))
                .header("Authorization", "Basic " + basicAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(form, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            log.info("Twilio SMS sent successfully to {}. SID response: {}", e164Phone, response.body());
        } else {
            log.warn("Twilio returned non-200 status {}: {}", response.statusCode(), response.body());
            logSimulatedOtp(phone, otp, "Twilio error status: " + response.statusCode());
        }
    }

    /**
     * Dispatches OTP using 2Factor.in API
     */
    private void send2FactorSms(String phone, String otp) throws Exception {
        if (twoFactorApiKey == null || twoFactorApiKey.isBlank() || twoFactorApiKey.startsWith("YOUR_")) {
            logSimulatedOtp(phone, otp, "2Factor API Key not configured in application.properties");
            return;
        }

        String url = String.format("https://2factor.in/v3/%s/SMS/%s/%s/CartIT_OTP",
                twoFactorApiKey.trim(),
                phone,
                otp
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            log.info("2Factor OTP sent successfully to {}. Response: {}", phone, response.body());
        } else {
            log.warn("2Factor returned non-200 status {}: {}", response.statusCode(), response.body());
            logSimulatedOtp(phone, otp, "2Factor error status: " + response.statusCode());
        }
    }

    /**
     * Logs OTP clearly to the terminal for debugging / development
     */
    private void logSimulatedOtp(String phone, String otp, String reason) {
        System.out.println("==================================================");
        System.out.println(" CartIT SMS GATEWAY DISPATCH");
        System.out.println(" Phone   : +91 " + phone);
        System.out.println(" OTP     : " + otp);
        System.out.println(" Message : Your CartIT verification code is " + otp + ".");
        System.out.println(" Note    : " + reason);
        System.out.println("==================================================");
    }

    /**
     * Cleans up phone number to 10-digit format
     */
    private String normalizeIndianPhone(String phone) {
        if (phone == null) return "";
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.length() == 12 && digits.startsWith("91")) {
            return digits.substring(2);
        }
        if (digits.length() == 11 && digits.startsWith("0")) {
            return digits.substring(1);
        }
        return digits;
    }
}
