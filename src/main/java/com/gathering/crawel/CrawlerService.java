package com.gathering.crawel;

import com.gathering.book.model.entity.Book;
import com.gathering.book.model.entity.BookCategory;
import com.gathering.book.repository.BookRepository;
import com.gathering.category.model.entity.Category;
import com.gathering.category.repository.CategoryJpaRepository;
import com.gathering.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public void saveBooksFromCrawler(String categoryName, List<String> titles, List<String> images, List<String> authors, List<String> publishers, List<String> bookPublishDates) {
        // 카테고리 찾거나 생성
        if(categoryRepository.existCategoryByName(categoryName)) return;

        Category category = Category.createCategory(categoryName);
        categoryRepository.save(category);
        // 책 데이터 저장
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            String image = images.size() > i ? images.get(i) : null;
            String author = authors.size() > i ? authors.get(i) : "저자 미제공";
            String publisher = publishers.size() > i ? publishers.get(i) : "출판사 미제공";
            String publishDateStr = bookPublishDates.size() > i ? bookPublishDates.get(i) : "출판일 미제공";

//            YearMonth publisherDate = parsePublishDate(publishDateStr);

            // 책 이미 저장되어있으면 건너뛰기
            if(bookRepository.existsByTitle(title)) continue;

            // BookCategory 생성 및 저장
            BookCategory bookCategory = BookCategory.createBookCategory(category);
            Book book = Book.createBook(title, image, author, publisher, bookCategory, publishDateStr);

            bookRepository.save(book);
        }
    }

//    private YearMonth parsePublishDate(String publishDateStr) {
//        if ("출판일 미제공".equals(publishDateStr)) {
//            return null;  // 출판일이 제공되지 않으면 null 반환
//        }
//
//        // "2014년 5월" 형식의 문자열을 처리할 때, 월을 두 자릿수로 맞추기
//        String[] parts = publishDateStr.replace("년", "").replace("월", "").split(" ");
//        String year = parts[0];  // 년도
//        String month = String.format("%02d", Integer.parseInt(parts[1]));  // 두 자릿수로 월 맞추기
//
//        // 새로운 형식으로 YearMonth 객체를 생성하여 반환
//        return YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
//    }
}
