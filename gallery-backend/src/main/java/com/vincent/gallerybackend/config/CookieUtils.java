package com.vincent.gallerybackend.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${jwt.expiration-time}")
    private int expirationTime;

    private static final String TOKEN_STRING = "token";

    public void createJwtCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie(TOKEN_STRING, jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(expirationTime);
        // jwtCookie.setSecure(true); -> nur bei HTTPS
        response.addCookie(jwtCookie);
    }

    public void deleteJwtCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie(TOKEN_STRING, null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        String jwt = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_STRING.equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        return jwt;
    }

}
