package com.gathering.user.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.model.entitiy.UserAttendance;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAttendanceBookJpaRepository extends JpaRepository<UserAttendanceBook, Long> {

    Optional<UserAttendanceBook> findByUserAttendanceAndGathering(UserAttendance userAttendance, Gathering gathering);
}
