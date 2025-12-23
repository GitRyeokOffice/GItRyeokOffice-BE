package com.example.gitryeokoffice.global.util;

import com.example.gitryeokoffice.user.exception.UserErrorCode;
import com.example.gitryeokoffice.user.exception.UserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 쿠키 관련 유틸리티 클래스
 */
public class CookieUtil {

    private static final String TOKEN_COOKIE_NAME = "accessToken";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 7일

    /**
     * 쿠키 배열에서 JWT 토큰 추출
     */
    public static String extractToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new UserException(UserErrorCode.MISSING_TOKEN);
        }

        for (Cookie cookie : cookies) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new UserException(UserErrorCode.MISSING_TOKEN);
    }

    /**
     * 응답에 JWT 토큰 쿠키 추가
     */
    public static void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    /**
     * 토큰 쿠키 삭제 (로그아웃)
     */
    public static void removeTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
