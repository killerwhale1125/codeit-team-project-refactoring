package com.gathering;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.book.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final BookRepository bookRepository;

    @PostConstruct
    void init() {
        BookDomain bookDomain1 = BookDomain.builder()
                .title("책 제목1")
                .image("http://localhost:8080/book-image")
                .publisher("출판사1")
                .author("책 저자1")
                .publishDate("2023-01-25")
                .selectedCount(0L)
                .star(3.5)
                .introduce("책 소개1")
                .totalPage(260)
                .build();

        BookDomain bookDomain2 = BookDomain.builder()
                .title("책 제목2")
                .image("http://localhost:8080/book-image")
                .publisher("출판사2")
                .author("책 저자2")
                .publishDate("2023-01-25")
                .selectedCount(0L)
                .star(3.5)
                .introduce("책 소개2")
                .totalPage(260)
                .build();

        BookDomain bookDomain3 = BookDomain.builder()
                .title("책 제목3")
                .image("http://localhost:8080/book-image")
                .publisher("출판사3")
                .author("책 저자3")
                .publishDate("2023-01-25")
                .selectedCount(0L)
                .star(3.5)
                .introduce("책 소개3")
                .totalPage(260)
                .build();

        bookRepository.save(bookDomain1);
        bookRepository.save(bookDomain2);
        bookRepository.save(bookDomain3);
    }
}
