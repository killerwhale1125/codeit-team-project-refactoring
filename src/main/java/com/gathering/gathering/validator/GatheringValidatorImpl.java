package com.gathering.gathering.validator;

import com.gathering.common.base.exception.BaseException;
import org.springframework.stereotype.Component;

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
    public void validateOwner(Long ownerId, Long userId) {
        if(ownerId != userId) throw new BaseException(ACCESS_DENIED);
    }

    @Override
    public boolean validateMinCapacity(int minCapacity) {
        return minCapacity <= 0 ? false : true;
    }

    @Override
    public boolean validateMaxCapacity(int maxCapacity) {
        return maxCapacity <= 0 ? false : true;
    }

    @Override
    public boolean validateCapacityRange(int minCapacity, int maxCapacity) {
        return maxCapacity < minCapacity ? false : true;
    }
}
