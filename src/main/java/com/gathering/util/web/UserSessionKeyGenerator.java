package com.gathering.util.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 유저 고유 정보 식별 클래스
 */
public class UserSessionKeyGenerator {
    public static String generateUserKey(HttpServletRequest request, HttpServletResponse response) {
        // IP 주소
        String ipAddress = WebRequestUtil.extractClientIp(request);
        // 브라우저
        String browser = WebRequestUtil.extractBrowser(request);

        String browserCookie = CookieUtil.getBrowserCookie(request, response);

        // 조합하여 고유 키 생성
        return ipAddress + ":" + browser + ":" + browserCookie;
    }
}
