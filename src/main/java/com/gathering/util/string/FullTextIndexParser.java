package com.gathering.util.string;

/**
 * Full Text Search 시 조건 설정 클래스
 * + -> 반드시 포함
 * * -> ex ) 이재명 책제목이라고 할 때 '이재' 라고 검색 시 이재명까지 같이 가져옴
 */
public class FullTextIndexParser {

    public static String formatForFullTextQuery(String searchWord) {
        return "+" + searchWord + "*";
    }
}
