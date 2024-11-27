package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.dto.GetAccessTokenDto;

public interface UserService {
    String getAccessToken(GetAccessTokenDto getAccessTokenDto) throws BaseException;
}
