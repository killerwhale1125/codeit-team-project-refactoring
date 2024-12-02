package com.gathering.gathering.validator;

public interface GatheringValidator {
    void validateCapacity(int minCapacity, int maxCapacity);
    void validateCapacityLimit(int currentCapacity, int maxCapacity);
    void validateOwner(Long ownerId, Long userId);
}
