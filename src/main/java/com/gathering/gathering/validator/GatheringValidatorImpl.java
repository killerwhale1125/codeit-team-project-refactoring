package com.gathering.gathering.validator;

import com.gathering.common.base.exception.BaseException;
import org.springframework.stereotype.Component;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Component
public class GatheringValidatorImpl implements GatheringValidator {
    @Override
    public void validateCapacity(int minCapacity, int maxCapacity) {
        if (minCapacity <= 0) throw new BaseException(INVALID_MIN_CAPACITY);
        if (maxCapacity <= 0) throw new BaseException(INVALID_MAX_CAPACITY);
        if (maxCapacity < minCapacity) throw new BaseException(INVALID_CAPACITY_RANGE);
    }

    @Override
    public void validateCapacityLimit(int currentCapacity, int maxCapacity) {
        if(currentCapacity + 1 > maxCapacity) {
            throw new BaseException(EXCEEDS_CAPACITY);
        }
    }

    @Override
    public void validateOwner(Long ownerId, Long userId) {
        if(ownerId != userId) throw new BaseException(ACCESS_DENIED);
    }
}
