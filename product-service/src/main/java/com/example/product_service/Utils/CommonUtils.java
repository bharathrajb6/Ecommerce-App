package com.example.product_service.Utils;

import java.util.UUID;

public class CommonUtils {
    public static boolean UUIDChecker(String value) {
        try {
            UUID.fromString(value);
            return true; // The string is a valid UUID.
        } catch (IllegalArgumentException exception) {
            return false; // The given string is not a UUID.
        }
    }
}
