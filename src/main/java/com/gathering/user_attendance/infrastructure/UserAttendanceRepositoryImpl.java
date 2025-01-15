package com.gathering.user_attendance.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user_attendance.domain.UserAttendanceDomain;
import com.gathering.user_attendance.infrastructure.entity.UserAttendance;
import com.gathering.user_attendance.service.port.UserAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class UserAttendanceRepositoryImpl implements UserAttendanceRepository {

    private final UserAttendanceJpaRepository userAttendanceJpaRepository;

    @Override
    public UserAttendanceDomain findByUserIdAndCreateDateWithAttendanceBooksAndGathering(long userId, LocalDate today) {
        return userAttendanceJpaRepository.findByUserIdAndCreateDateWithAttendanceBooksAndGathering(userId, today)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_USER_ATTENDANCE))
                .toEntity();
    }

    @Override
    public UserAttendanceDomain findByUserIdAndCreateDate(Long userId, LocalDate today) {
        return userAttendanceJpaRepository.findByUserIdAndCreateDate(userId, today)
                .map(UserAttendance::toEntity)
                .orElse(null);
    }

    @Override
    public void insert(Long userId) {
        userAttendanceJpaRepository.insert(userId);
    }
}
