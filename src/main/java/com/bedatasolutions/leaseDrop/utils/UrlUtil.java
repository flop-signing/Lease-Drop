package com.bedatasolutions.leaseDrop.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UrlUtil {
    public static String urlBase64Encode(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String urlBase64Decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value));
    }
}