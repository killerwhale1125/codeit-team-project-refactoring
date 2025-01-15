package com.gathering.user.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.domain.SingUpType;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.domain.UserResponse;
import com.gathering.user.infrastructure.entitiy.User;
import com.gathering.user.service.port.UserRepository;
import com.gathering.user_attendance.infrastructure.UserAttendanceJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_USER;

@Repository("userRepository")
@RequiredArgsConstructor
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserAttendanceJpaRepository userAttendanceJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public UserDomain selectUser(String userName) {
        return userJpaRepository.findByUserName(userName).orElseThrow(() -> new BaseException(NOT_EXISTED_USER)).toEntity();
    }

    @Override
    public UserDomain findByUsername(String username) {
        return userJpaRepository.findByUserName(username)
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER))
                .toEntity();
    }

    @Override
    public boolean checkType(String param, SingUpType type) {
        Optional<User> user = switch (type) {
            case EMAIL -> userJpaRepository.findByEmail(param);
            case ID -> userJpaRepository.findByUserName(param);
        };
        return user.isEmpty();
    }

    @Override
    public UserDomain save(UserDomain user) {
        return userJpaRepository.save(User.fromEntity(user)).toEntity();
    }

    @Override
    public UserDomain selectUserByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER))
                .toEntity();
    }

    @Override
    public Set<Long> findWishGatheringIdsByUserName(String username) {
        return userJpaRepository.findWishGatheringIdsByUserName(username);
    }

    @Override
    public UserDomain findByUsernameWithImage(String username) {
        return userJpaRepository.findByUsernameWithImage(username)
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER))
                .toEntity();
    }
}
