package com.gathering.util.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.UUID;

public class CookieUtil {

    private static String generateBrowserCookie(HttpServletResponse response) {
        // 브라우저 고유 식별자로 UUID 사용
        Cookie cookie = new Cookie("browser_id", UUID.randomUUID().toString());
        cookie.setHttpOnly(true); // JS에서 접근 불가
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유효
        response.addCookie(cookie);
        return cookie.getValue();
    }

    public static String getBrowserCookie(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "browser_id".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst().get();
        }
        return generateBrowserCookie(response);
    }
}
