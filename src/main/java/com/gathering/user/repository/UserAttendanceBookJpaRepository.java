package com.gathering.user.repository;

import com.gathering.user_attendance_book.model.entity.UserAttendanceBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserAttendanceBookJpaRepository extends JpaRepository<UserAttendanceBook, Long> {

    @Query("SELECT count(uab.id) FROM UserAttendanceBook uab " +
            "LEFT JOIN uab.userAttendance ua " +
            "WHERE ua.user.id = :userId " +
            "AND ua.createDate BETWEEN :startDate " +
            "AND :endDate AND uab.book.id = :bookId")
    long getByUserIdAndBetweenDate(long userId, LocalDate startDate, LocalDate endDate, Long bookId);
}
