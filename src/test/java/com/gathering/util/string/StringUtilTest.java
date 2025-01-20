package com.gathering.util.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {

    @Test
    @DisplayName("문자열이 Null 이 아니며 지정한 최소 길이보다 같거나 길면 True 를 반환한다.")
    void isValidLengthSuccess() {
        /* given */
        String str = "문자열";

        /* when */
        boolean result = StringUtil.isValidLength(str, 3);

        /* then */
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("문자열이 null 일때 False 를 반환한다.")
    void isValidLengthFailWhenNull() {
        /* given */
        String str = null;
        int minLength = 3;

        /* when */
        boolean result = StringUtil.isValidLength(str, minLength);

        /* then */
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("문자열이 null 일때 False 를 반환한다.")
    void isValidLengthFailWhenShortest() {
        /* given */
        String str = "문자";
        int minLength = 3;
        /* when */
        boolean result = StringUtil.isValidLength(str, minLength);

        /* then */
        assertThat(result).isFalse();
    }
}
