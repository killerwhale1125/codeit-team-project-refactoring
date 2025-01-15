package com.gathering.gathering.util;

import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.user.domain.UserDomain;

import java.time.LocalDate;
import java.util.List;

public interface GatheringValidator {
    void validateCapacityLimit(int currentCapacity, int maxCapacity);
    void validateOwner(String ownerId, String userId);

    boolean validateMinCapacity(int minCapacity);

    boolean validateMaxCapacity(int maxCapacity);

    boolean validateCapacityRange(int minCapacity, int maxCapacity);

    void validateJoinDate(LocalDate now, LocalDate localDate);

    void validateAlreadyJoinedUser(List<GatheringUserDomain> gatheringUsers, UserDomain user);

}
