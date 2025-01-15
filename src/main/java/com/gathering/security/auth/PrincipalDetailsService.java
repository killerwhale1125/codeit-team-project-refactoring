package com.gathering.security.auth;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.infrastructure.entitiy.User;
import com.gathering.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_USER;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = userJpaRepository.findByUserName(userName).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
        return new PrincipalDetails(user);
    }
}
