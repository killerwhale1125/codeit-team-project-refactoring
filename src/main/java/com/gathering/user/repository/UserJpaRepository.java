package com.gathering.user.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUserName(String userName);

    default User findByUserNameOrThrow(String userName) {
        return  findByUserName(userName).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_EXISTED_USER));
    }

    default User findByEmailOrThrow(String email) {
        return  findByEmail(email).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_EXISTED_USER));
    }
}
