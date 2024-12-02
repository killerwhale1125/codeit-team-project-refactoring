package com.gathering.book.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;
    private String image;
    private String publisher;
    private LocalDate publishDate;
    private long selectedCount;

    // 모임에 책이 선택될 때마다 카운트를 증가 ( Best 독서 모임 조회용 )
    public void incrementSelectedCount() {
        this.selectedCount++;
    }
}
