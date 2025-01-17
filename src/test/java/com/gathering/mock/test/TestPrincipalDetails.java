package com.gathering.mock.test;

import com.gathering.user.domain.UserDomain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TestPrincipalDetails implements UserDetails {

    private UserDomain user;

    public TestPrincipalDetails(UserDomain user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }
}
