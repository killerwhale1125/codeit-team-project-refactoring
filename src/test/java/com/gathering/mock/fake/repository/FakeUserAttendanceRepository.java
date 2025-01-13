package com.gathering.mock.fake.repository;

import com.gathering.user_attendance.model.domain.UserAttendanceDomain;
import com.gathering.user_attendance.repository.UserAttendanceRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserAttendanceRepository implements UserAttendanceRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<UserAttendanceDomain> data = new ArrayList<>();

    @Override
    public UserAttendanceDomain findByUserIdAndCreateDateWithAttendanceBooksAndGathering(long userId, LocalDate today) {
        return data.stream().filter(item -> Objects.equals(item.getUser().getId(), userId)
                && Objects.equals(item.getCreateDate(), Date.valueOf(today)))
                .findAny()
                .get();
    }

    public UserAttendanceDomain save(UserAttendanceDomain userAttendance) {
        if(Objects.isNull(userAttendance.getId())) {
            UserAttendanceDomain createUserAttendance = UserAttendanceDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .user(userAttendance.getUser())
                    .createDate(Date.valueOf(LocalDate.now()))
                    .userAttendanceBooks(new ArrayList<>())
                    .build();
            data.add(createUserAttendance);
            return createUserAttendance;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), userAttendance.getId()));
            data.add(userAttendance);
            return userAttendance;
        }
    }
}
