package com.gathering.user_attendance_book.repository;

import com.gathering.user.repository.UserAttendanceBookJpaRepository;
import com.gathering.user_attendance_book.model.domain.UserAttendanceBookDomain;
import com.gathering.user_attendance_book.model.entity.UserAttendanceBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class UserAttendanceBookRepositoryImpl implements UserAttendanceBookRepository{

    private final UserAttendanceBookJpaRepository userAttendanceBookJpaRepository;

    @Override
    public UserAttendanceBookDomain save(UserAttendanceBookDomain userAttendanceBook) {
        return userAttendanceBookJpaRepository.save(UserAttendanceBook.fromEntity(userAttendanceBook)).toEntity();
    }

    @Override
    public long getByUserIdAndBetweenDate(Long userId, LocalDate startDate, LocalDate endDate, Long bookId) {
        return userAttendanceBookJpaRepository.getByUserIdAndBetweenDate(userId, startDate, endDate, bookId);
    }
}
