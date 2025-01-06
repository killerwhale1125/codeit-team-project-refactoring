package com.gathering.util.web;

import jakarta.servlet.http.HttpServletRequest;

public class WebRequestUtil {
    // 브라우저 정보 조회
    public static String extractBrowser(HttpServletRequest request) {
        String browser = "none";
        String userBrowser = request.getHeader("User-Agent");

        if(userBrowser.contains("Trident")) {												// IE
            browser = "ie";
        } else if(userBrowser.contains("Edge")) {											// Edge
            browser = "edge";
        } else if(userBrowser.contains("Whale")) { 										// Naver Whale
            browser = "whale";
        } else if(userBrowser.contains("Opera") || userBrowser.contains("OPR")) { 		// Opera
            browser = "opera";
        } else if(userBrowser.contains("Firefox")) { 										 // Firefox
            browser = "firefox";
        } else if(userBrowser.contains("Safari") && !userBrowser.contains("Chrome")) {	 // Safari
            browser = "safari";
        } else if(userBrowser.contains("Chrome")) {										 // Chrome
            browser = "chrome";
        }

        return browser;
    }

    // IP 정보 조회
    public static String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
