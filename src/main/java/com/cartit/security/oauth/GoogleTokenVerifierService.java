package com.cartit.security.oauth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenVerifierService {

    private static final Logger log = LoggerFactory.getLogger(GoogleTokenVerifierService.class);

    @Value("${google.oauth.client-ids:}")
    private String configuredClientIds;

    public GoogleIdToken.Payload verifyToken(String idTokenString) {
        if (idTokenString == null || idTokenString.isBlank()) {
            throw new IllegalArgumentException("Google ID Token cannot be empty");
        }

        try {
            NetHttpTransport transport = new NetHttpTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleIdTokenVerifier.Builder builder = new GoogleIdTokenVerifier.Builder(transport, jsonFactory);

            if (configuredClientIds != null && !configuredClientIds.isBlank()) {
                List<String> audiences = Arrays.stream(configuredClientIds.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                if (!audiences.isEmpty()) {
                    builder.setAudience(audiences);
                }
            }

            GoogleIdTokenVerifier verifier = builder.build();
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                log.warn("GoogleIdTokenVerifier returned null for provided token");
                throw new com.cartit.exception.UnauthorizedException("Invalid or unverified Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            if (payload == null || payload.getSubject() == null || payload.getSubject().isBlank()) {
                throw new com.cartit.exception.UnauthorizedException("Google ID token payload is missing 'sub' claim");
            }

            return payload;
        } catch (com.cartit.exception.UnauthorizedException ue) {
            throw ue;
        } catch (Exception e) {
            log.error("Google ID token verification error: {}", e.getMessage(), e);
            throw new com.cartit.exception.UnauthorizedException("Google ID token verification failed: " + e.getMessage());
        }
    }
}
