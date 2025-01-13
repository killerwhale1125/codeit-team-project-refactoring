package com.gathering.user_attendance_book.repository;

import com.gathering.user_attendance_book.model.domain.UserAttendanceBookDomain;

import java.time.LocalDate;
import java.util.List;

public interface UserAttendanceBookRepository {
    UserAttendanceBookDomain save(UserAttendanceBookDomain userAttendanceBookDomain);

    long getByUserIdAndBetweenDate(Long id, LocalDate startDate, LocalDate endDate, Long bookId);
}
