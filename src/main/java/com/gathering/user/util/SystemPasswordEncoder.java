package com.gathering.user.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemPasswordEncoder implements PasswordEncoderHolder {
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean verifyPassword(String loginPassword, String originPassword) {
        return passwordEncoder.matches(loginPassword, originPassword);
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
