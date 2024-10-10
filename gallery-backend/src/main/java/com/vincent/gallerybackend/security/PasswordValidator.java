package com.vincent.gallerybackend.security;

import java.util.regex.Pattern;

public final class PasswordValidator {

  /*
        Password must be at least 8 characters long, contain at least 1 uppercase letter,
        1 digit, and 1 special character (any non-alphanumeric character).
    */

    private static final String PASSWORD_PATTERN
            = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isPasswordStrong(String password) {
        return pattern.matcher(password).matches();
    }

}
