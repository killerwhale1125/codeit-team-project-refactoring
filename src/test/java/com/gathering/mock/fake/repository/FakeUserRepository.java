package com.gathering.mock.fake.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.UserAttendance;
import com.gathering.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_USER;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<UserDomain> data = new ArrayList<>();

    @Override
    public UserDto selectUser(String userName) {
        return null;
    }

    @Override
    public int insertAttendance(long usersId) {
        return 0;
    }

    @Override
    public UserDomain findByUsername(String username) {
        return data.stream().filter(item -> Objects.equals(item.getUserName(), username))
                .findFirst()
                .orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {

    }

    @Override
    public boolean checkType(String param, boolean typeBol) {
        return false;
    }

    @Override
    public UserDto editUser(EditUserRequestDto editUserRequestDto, String fileName, long userId) {
        return null;
    }

    @Override
    public UserDomain save(UserDomain userDomain) {
        if (Objects.isNull(userDomain.getId())) {
            final UserDomain createUser = UserDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .id(1L)
                    .userName(userDomain.getUserName())
                    .password(userDomain.getPassword())
                    .email(userDomain.getEmail())
                    .profile(userDomain.getProfile())
                    .roles(userDomain.getRoles())
                    .build();
            data.add(createUser);
            return createUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), userDomain.getId()));
            data.add(userDomain);
            return userDomain;
        }
    }

    @Override
    public UserDto selectUserByEmail(String email) {
        return null;
    }

    @Override
    public Set<Long> findWishGatheringIdsByUserName(String username) {
        return data.stream()
                .filter(item -> Objects.equals(item.getUserName(), username))
                .findFirst()
                .get()
                .getWishGatheringIds();
    }

    @Override
    public boolean existsByUserName(String username) {
        return false;
    }

    @Override
    public List<UserAttendance> getUserAttendancesByUserIdAndDate(Long id, LocalDate startDate, LocalDate endDate) {
        return null;
    }

}
