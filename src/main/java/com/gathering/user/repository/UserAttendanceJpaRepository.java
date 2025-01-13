package com.gathering.user.repository;

import com.gathering.user_attendance.model.entity.UserAttendance;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface UserAttendanceJpaRepository extends JpaRepository<UserAttendance, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USER_ATTENDANCE (USER_ID, CREATE_DATE) VALUES (:userId, DATE_FORMAT(NOW(), '%Y-%m-%d')) " +
            "ON DUPLICATE KEY UPDATE USER_ID = :userId", nativeQuery = true)
    int insertAttendance(@Param("userId") Long userId);

    Optional<UserAttendance> findByUserIdAndCreateDate(long userId, LocalDate today);

    @EntityGraph(attributePaths = {"userAttendanceBooks", "userAttendanceBooks.gathering"})
    @Query("SELECT ua FROM UserAttendance ua LEFT JOIN ua.user u WHERE u.id = :userId AND createDate = :today")
    Optional<UserAttendance> findByUserIdAndCreateDateWithAttendanceBooksAndGathering(@Param("userId") long userId, @Param("today") LocalDate today);
}
