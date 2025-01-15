package com.gathering.user.service.port;

import com.gathering.user.domain.SingUpType;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.domain.UserResponse;

import java.util.Set;

public interface UserRepository {
    
    // 사용자 정보 조회
    UserDomain selectUser(String userName);

    // 출석 체크
    UserDomain findByUsername(String username);

    // 이메일/아이디 체크
    boolean checkType(String param, SingUpType typeBol);

    UserDomain save(UserDomain user);

    // 이메일로 사용자 찾기
    UserDomain selectUserByEmail(String email);

    Set<Long> findWishGatheringIdsByUserName(String username);

    UserDomain findByUsernameWithImage(String username);
}
