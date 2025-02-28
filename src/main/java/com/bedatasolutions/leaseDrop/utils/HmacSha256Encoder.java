package com.bedatasolutions.leaseDrop.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class HmacSha256Encoder {

    public static String calculateSecretHash(String username, String clientId, String clientSecret) {
        try {
            String message = username + clientId;
            SecretKeySpec secretKeySpec = new SecretKeySpec(clientSecret.getBytes(), "HmacSHA256");

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hmacSha256 = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hmacSha256);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC-SHA256 hash", e);
        }
    }

    public static void main(String[] args) {
        String username = "testuser";
        String clientId = "your-client-id";
        String clientSecret = "your-client-secret";

        String secretHash = calculateSecretHash(username, clientId, clientSecret);
        System.out.println("Secret Hash: " + secretHash);
    }
}
