package com.gathering.user_attendance_book.service.port;

import com.gathering.user_attendance_book.domain.UserAttendanceBookDomain;

import java.time.LocalDate;

public interface UserAttendanceBookRepository {
    UserAttendanceBookDomain save(UserAttendanceBookDomain userAttendanceBookDomain);

    long getByUserIdAndBetweenDate(Long id, LocalDate startDate, LocalDate endDate, Long bookId);
}
