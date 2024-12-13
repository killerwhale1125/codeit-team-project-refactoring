package com.gathering.book.service;

import com.gathering.book.model.dto.BookSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    List<BookSearchResponse> findPopularBooks(Pageable pageable);
}
