package com.gathering.book.service;

import com.gathering.book.controller.port.BookService;
import com.gathering.book.controller.response.BookResponse;
import com.gathering.book.controller.response.BookSearchResponse;
import com.gathering.book.service.port.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookSearchResponse> findPopularBooks(int page, int size) {
        return bookRepository.findPopularBooks(page, size);
    }

    @Override
    public List<BookResponse> searchBooksBySearchWord(String searchWord) {
        return bookRepository.searchBooksBySearchWord(searchWord).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
