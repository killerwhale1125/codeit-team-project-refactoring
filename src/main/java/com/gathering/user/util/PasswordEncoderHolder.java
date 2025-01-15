package com.gathering.user.util;

public interface PasswordEncoderHolder {
    boolean verifyPassword(String password, String password1);

    String encode(String password);

}
