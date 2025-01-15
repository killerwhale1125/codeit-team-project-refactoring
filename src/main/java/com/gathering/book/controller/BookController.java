package com.gathering.book.controller;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book.controller.response.BookSearchResponse;
import com.gathering.book.controller.port.BookService;
import com.gathering.common.base.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/popular")
    @Operation(summary = "메인페이지 인기 책 조회", description = "Notion 참고")
    public BaseResponse<List<BookSearchResponse>> popular(@RequestParam int page, @RequestParam int size) {
        return new BaseResponse<>(bookService.findPopularBooks(page, size));
    }

    @GetMapping("/title-search")
    @Operation(summary = "책 이름으로 조회", description = "Notion 참고")
    public BaseResponse<List<BookResponse>> searchBooksBySearchWord(@RequestParam String searchWord) {
        return new BaseResponse<>(bookService.searchBooksBySearchWord(searchWord));
    }
}
