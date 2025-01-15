package com.gathering.user_attendance.service.port;

import com.gathering.user_attendance.domain.UserAttendanceDomain;

import java.time.LocalDate;

public interface UserAttendanceRepository {

    UserAttendanceDomain findByUserIdAndCreateDateWithAttendanceBooksAndGathering(long userId, LocalDate today);

    UserAttendanceDomain findByUserIdAndCreateDate(Long userId, LocalDate today);

    void insert(Long userId);

}
