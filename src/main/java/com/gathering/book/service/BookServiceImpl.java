package com.gathering.book.service;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookSearchResponse> findPopularBooks(Pageable pageable) {
        return bookRepository.findPopularBooks(pageable);
    }

    @Override
    public List<BookResponse> searchBooksBySearchWord(String searchWord) {
        return bookRepository.searchBooksBySearchWord(searchWord).stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
