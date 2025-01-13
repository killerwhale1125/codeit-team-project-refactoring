package com.gathering.user_attendance.repository;

import com.gathering.user_attendance.model.domain.UserAttendanceDomain;

import java.time.LocalDate;
import java.util.List;

public interface UserAttendanceRepository {

    UserAttendanceDomain findByUserIdAndCreateDateWithAttendanceBooksAndGathering(long userId, LocalDate today);
}
