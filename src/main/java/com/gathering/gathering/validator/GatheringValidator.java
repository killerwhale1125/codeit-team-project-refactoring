package com.gathering.gathering.validator;

import java.time.LocalDate;

public interface GatheringValidator {
    void validateCapacityLimit(int currentCapacity, int maxCapacity);
    void validateOwner(String ownerId, String userId);

    boolean validateMinCapacity(int minCapacity);

    boolean validateMaxCapacity(int maxCapacity);

    boolean validateCapacityRange(int minCapacity, int maxCapacity);

    void validateJoinDate(LocalDate now, LocalDate localDate);
}
