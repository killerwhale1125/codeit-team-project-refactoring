package com.gathering.gathering.validator;

public interface GatheringValidator {
    void validateCapacityLimit(int currentCapacity, int maxCapacity);
    void validateOwner(String ownerId, String userId);

    boolean validateMinCapacity(int minCapacity);

    boolean validateMaxCapacity(int maxCapacity);

    boolean validateCapacityRange(int minCapacity, int maxCapacity);
}
