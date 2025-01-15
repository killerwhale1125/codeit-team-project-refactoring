package com.gathering.user_attendance_book.domain;

import com.gathering.book.domain.BookDomain;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.user_attendance.domain.UserAttendanceDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAttendanceBookDomain {
    private Long id;
    private UserAttendanceDomain userAttendance;
    private BookDomain book;
    private GatheringDomain gathering;

    public static UserAttendanceBookDomain create(UserAttendanceDomain userAttendance, BookDomain book, GatheringDomain gathering) {
        return UserAttendanceBookDomain.builder()
                .userAttendance(userAttendance)
                .book(book)
                .gathering(gathering)
                .build();
    }
}
