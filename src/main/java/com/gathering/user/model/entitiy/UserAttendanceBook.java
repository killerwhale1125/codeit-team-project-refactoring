package com.gathering.user.model.entitiy;

import com.gathering.book.model.entity.Book;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class UserAttendanceBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATNDC_BOOK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATNDC_ID")
    private UserAttendance userAttendance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GATHERING_ID")
    private Gathering gathering;


    public static UserAttendanceBook createUserAttendanceBook(UserAttendance userAttendance, Book book, Gathering gathering) {
        UserAttendanceBook userAttendanceBook = new UserAttendanceBook();
        userAttendanceBook.userAttendance = userAttendance;
        userAttendanceBook.book = book;
        userAttendanceBook.gathering = gathering;

        return userAttendanceBook;
    }

}
