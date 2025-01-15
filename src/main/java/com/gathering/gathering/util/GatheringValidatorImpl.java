package com.gathering.gathering.util;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.user.domain.UserDomain;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Component
public class GatheringValidatorImpl implements GatheringValidator {

    @Override
    public void validateCapacityLimit(int currentCapacity, int maxCapacity) {
        if(currentCapacity + 1 > maxCapacity) {
            throw new BaseException(EXCEEDS_CAPACITY);
        }
    }

    @Override
    public void validateOwner(String owner, String user) {
        if(!owner.equals(user)) throw new BaseException(ACCESS_DENIED);
    }

    @Override
    public boolean validateMinCapacity(int minCapacity) {
        return minCapacity <= 0 ? true : false;
    }

    @Override
    public boolean validateMaxCapacity(int maxCapacity) {
        return maxCapacity <= 0 ? true : false;
    }

    @Override
    public boolean validateCapacityRange(int minCapacity, int maxCapacity) {
        return maxCapacity < minCapacity ? true : false;
    }

    @Override
    public void validateJoinDate(LocalDate startDate, LocalDate now) {
        if(now.isEqual(startDate) || now.isAfter(startDate)) {
            throw new BaseException(RECRUITMENT_CLOSED);
        }
    }

    @Override
    public void validateAlreadyJoinedUser(List<GatheringUserDomain> gatheringUsers, UserDomain user) {
        if(gatheringUsers.stream()
                .anyMatch(gatheringUser -> gatheringUser.getUser().getId() == user.getId())) {
            throw new BaseException(ALREADY_JOINED);
        }
    }

}
