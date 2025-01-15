package com.gathering.user.infrastructure;

import com.gathering.util.web.WebRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserRedisKey {
    public static final String USER_REFRESH_TOKEN_PREFIX = "user:refreshToken:";


    public static String getUserRefreshTokenKey(HttpServletRequest request, HttpServletResponse response) {
        // IP 주소
        String ipAddress = WebRequestUtil.extractClientIp(request);
        // 브라우저
        String browser = WebRequestUtil.extractBrowser(request);

        return USER_REFRESH_TOKEN_PREFIX + ipAddress + ":" + browser;
    }
}
