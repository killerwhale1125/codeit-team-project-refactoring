package com.gathering.util.string;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FullTextIndexParserTest {

    @Test
    @DisplayName("DB FullText Index 조건에 맞게 문자열을 변형시킨다.")
    void formatForFullTextQuery() {
        /* given */
        String searchWord = "검색어";

        /* when */
        String formatSearchWord = FullTextIndexParser.formatForFullTextQuery(searchWord);

        /* then */
        assertThat(formatSearchWord).isEqualTo("+검색어*");
    }
}
