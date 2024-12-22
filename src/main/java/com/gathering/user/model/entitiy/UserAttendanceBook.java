package com.gathering.user.model.entitiy;

import com.gathering.book.model.entity.Book;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class UserAttendanceBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATNDC_BOOK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATNDC_ID")
    private UserAttendance userAttendance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;
}
