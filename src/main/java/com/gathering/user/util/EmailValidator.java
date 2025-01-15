package com.gathering.user.util;

public class EmailValidator {
    // 이메일 체크 메서드
    public static boolean isValidEmail(String email) {
        // 기본적인 이메일 형식 체크 (정규식이나 추가 로직을 사용할 수 있습니다)
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}
