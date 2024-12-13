package com.gathering.book.service;

import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookSearchResponse> findPopularBooks(Pageable pageable) {
        return bookRepository.findPopularBooks(pageable);
    }
}
