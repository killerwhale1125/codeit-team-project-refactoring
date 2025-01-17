package com.gathering.mock.fake.repository;

import com.gathering.user_attendance.domain.UserAttendanceDomain;
import com.gathering.user_attendance_book.domain.UserAttendanceBookDomain;
import com.gathering.user_attendance_book.service.port.UserAttendanceBookRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserAttendanceBookRepository implements UserAttendanceBookRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<UserAttendanceBookDomain> data = new ArrayList<>();

    @Override
    public UserAttendanceBookDomain save(UserAttendanceBookDomain userAttendanceBook) {
        if(Objects.isNull(userAttendanceBook.getId())) {
            UserAttendanceDomain userAttendance = userAttendanceBook.getUserAttendance();
            UserAttendanceBookDomain createAttendanceBook = UserAttendanceBookDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .userAttendance(userAttendanceBook.getUserAttendance())
                    .book(userAttendanceBook.getBook())
                    .gathering(userAttendanceBook.getGathering())
                    .build();
            userAttendance.getUserAttendanceBooks().add(createAttendanceBook);
            data.add(createAttendanceBook);
            return createAttendanceBook;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), userAttendanceBook.getId()));
            data.add(userAttendanceBook);
            return userAttendanceBook;
        }
    }

    @Override
    public long getByUserIdAndBetweenDate(Long id, LocalDate startDate, LocalDate endDate, Long bookId) {
        return 0;
    }
}
