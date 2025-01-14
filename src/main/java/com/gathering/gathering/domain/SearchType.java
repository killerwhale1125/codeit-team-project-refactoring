package com.gathering.gathering.domain;

public enum SearchType {
    TITLE("제목"),
    CONTENT("내용"),
    BOOK_NAME("도서명");

    private final String type;

    SearchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
