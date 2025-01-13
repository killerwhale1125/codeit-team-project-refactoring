package com.gathering.user.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.QUser;
import com.gathering.user.model.entitiy.User;
import com.gathering.user_attendance.model.entity.UserAttendance;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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
    public UserDto selectUser(String userName) {
        User user = userJpaRepository.findByUserName(userName).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        return UserDto.fromEntity(user);
    }

    @Override
    public int insertAttendance(long userId) {
        LocalDate today = LocalDate.now();

        Optional<UserAttendance> existingAttendance =
                userAttendanceJpaRepository.findByUserIdAndCreateDate(userId, today);

        int result = 0;
        if(existingAttendance.isEmpty()) {
            result = userAttendanceJpaRepository.insertAttendance(userId);
        }

        return result;
    }

    @Override
    public UserDomain findByUsername(String username) {
        return userJpaRepository.findByUserName(username)
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER))
                .toEntity();
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.createUser(signUpRequestDto);
        userJpaRepository.save(user);
    }

    @Override
    public boolean checkType(String param, boolean typeBol) {
        Optional<User> user = typeBol ? userJpaRepository.findByEmail(param) : userJpaRepository.findByUserName(param);
        if (user.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public UserDto editUser(EditUserRequestDto editUserRequestDto, String fileName, long userId) {
        QUser quser = QUser.user;
        var updateClause = queryFactory.update(quser)
                .set(quser.userName, editUserRequestDto.getUserName())
                .set(quser.email, editUserRequestDto.getEmail());


        // 비밀번호 수정시
        String editPassword = editUserRequestDto.getPassword();
        if (editPassword != null && !editPassword.isEmpty()) {
            updateClause.set(quser.password, editPassword);
        }
        // fileName이 null이거나 빈 문자열이 아닌 경우에만 profile을 업데이트
        if (fileName != null && !fileName.trim().isEmpty()) {
            updateClause.set(quser.profile, fileName);
        }

        // Where 절 추가 및 실행
        long result = updateClause.where(quser.id.eq(userId))
                .execute();

        if(result != 0) {
            // 영속성 관리를 위해 업데이트 이후에는 clear
            em.clear();
            User user = userJpaRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));

            return UserDto.fromEntity(user);
        }

        return null;
    }

    @Override
    public UserDomain save(UserDomain user) {
        return userJpaRepository.save(User.fromEntity(user)).toEntity();
    }

    @Override
    public UserDto selectUserByEmail(String email) {
//        User user = userJpaRepository.findByEmailOrThrow(email);
        User user = userJpaRepository.findByEmail(email).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        return UserDto.fromEntity(user);
    }

    @Override
    public Set<Long> findWishGatheringIdsByUserName(String username) {
        return userJpaRepository.findWishGatheringIdsByUserName(username);
    }
}
