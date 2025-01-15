package com.gathering.user_attendance.domain;

import com.gathering.user.domain.UserDomain;
import com.gathering.user_attendance_book.domain.UserAttendanceBookDomain;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.util.List;

@Getter
@Builder
public class UserAttendanceDomain {
    private long id;
    private UserDomain user;
    private Date createDate;
    private List<UserAttendanceBookDomain> userAttendanceBooks;

}
