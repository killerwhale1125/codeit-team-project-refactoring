package com.gathering.util.string;

public class StringUtil {
    // 문자열이 null이 아니고 세 글자 이상인지 검증
    public static boolean isValidLength(String str, int minLength) {
        return str != null && str.length() >= minLength;
    }
}
