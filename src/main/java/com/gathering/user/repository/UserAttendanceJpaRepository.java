package com.gathering.user.repository;

import com.gathering.user.model.entitiy.User;
import com.gathering.user.model.entitiy.UserAttendance;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserAttendanceJpaRepository extends JpaRepository<UserAttendance, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USER_ATTENDANCE (USER_ID, CREATE_DATE) VALUES (:userId, DATE_FORMAT(NOW(), '%Y-%m-%d')) " +
            "ON DUPLICATE KEY UPDATE USER_ID = :userId", nativeQuery = true)
    int insertAttendance(@Param("userId") Long userId);


}
