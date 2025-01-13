package com.gathering.user_attendance_book.model.entity;

import com.gathering.book.model.entity.Book;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.user_attendance.model.entity.UserAttendance;
import com.gathering.user_attendance_book.model.domain.UserAttendanceBookDomain;
import jakarta.persistence.*;
import lombok.Getter;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

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

    public static UserAttendanceBook fromEntity(UserAttendanceBookDomain userAttendanceBook) {
        UserAttendanceBook userAttendanceBookEntity = new UserAttendanceBook();
        userAttendanceBookEntity.id = userAttendanceBook.getId();
        UserAttendance userAttendance = nullableEntity(UserAttendance::fromEntity, userAttendanceBook.getUserAttendance());

        if(userAttendance != null) {
            userAttendanceBookEntity.userAttendance = userAttendance;
            userAttendance.getUserAttendanceBooks().add(userAttendanceBookEntity);
        }

        Book book = nullableEntity(Book::fromEntity, userAttendanceBook.getBook());
        if(book != null) {
            userAttendanceBookEntity.book = book;
            book.getUserAttendanceBooks().add(userAttendanceBookEntity);
        }

        userAttendanceBookEntity.gathering = nullableEntity(Gathering::fromEntity, userAttendanceBook.getGathering());

        return userAttendanceBookEntity;
    }

    public UserAttendanceBookDomain toEntity() {
        UserAttendanceBookDomain.UserAttendanceBookDomainBuilder builder = UserAttendanceBookDomain.builder()
                .id(id);

        if (gathering != null && getPersistenceUtil().isLoaded(gathering)) {
            builder.gathering(gathering.toEntity());
        }

        return builder.build();
    }
}
