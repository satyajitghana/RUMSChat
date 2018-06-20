package com.msruas.debug.rumschat.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class Validation {
    // All function returns false if not a valid field

    // Generic field validation
    public static boolean validateField(String field) {
        return !TextUtils.isEmpty(field);
    }

    // Validates Email
    public static boolean validateEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // Validates Password
    public static boolean validatePassword(String pass) {
        return (pass.length() >= 6);
    }
}
