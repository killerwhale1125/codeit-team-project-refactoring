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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Transactional
    public void saveBooksFromCrawler(String categoryName, List<String> titles, List<String> images, List<String> authors, List<String> publishers, List<String> bookPublishDates, List<String> bookRatings) {
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
            String star = bookRatings.size() > i ? bookRatings.get(i) : "평점 미제공";
//            YearMonth publisherDate = parsePublishDate(publishDateStr);

            // 책 이미 저장되어있으면 건너뛰기
            if(bookRepository.existsByTitle(title)) continue;

            // BookCategory 생성 및 저장
            BookCategory bookCategory = BookCategory.createBookCategory(category);
            Book book = Book.createBook(title, image, author, publisher, bookCategory, publishDateStr, star);

            bookRepository.save(book);
        }
    }
}
