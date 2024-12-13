package com.gathering.book.controller;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.book.model.dto.BookSearchResponse;
import com.gathering.book.service.BookService;
import com.gathering.common.base.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@Tag(name = "책 관련 API", description = "책 관련 API")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class BookController {

    private final BookService bookService;

    @GetMapping("/popular")
    @Operation(summary = "메인페이지 인기 책 조회", description = "Notion 참고")
    public BaseResponse<List<BookSearchResponse>> popular(@PageableDefault(page = 0, size = 5,
            sort = "selectedCount",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return new BaseResponse<>(bookService.findPopularBooks(pageable));
    }

    @GetMapping("/title-search")
    @Operation(summary = "책 이름으로 조회", description = "Notion 참고")
    public BaseResponse<List<BookResponse>> searchBooksBySearchWord(@RequestParam String searchWord) {
        return new BaseResponse<>(bookService.searchBooksBySearchWord(searchWord));
    }
}
