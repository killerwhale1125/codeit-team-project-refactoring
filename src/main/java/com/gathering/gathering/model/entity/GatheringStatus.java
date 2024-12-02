package com.gathering.gathering.model.entity;

import com.gathering.common.base.exception.BaseException;

import static com.gathering.common.base.response.BaseResponseStatus.*;

public enum GatheringStatus {
    FULL {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_FULL);
        }
    },
    ACTIVE {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_ALREADY_STARTED);
        }
    },
    DELETED {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_ALREADY_DELETED);
        }
    },
    COMPLETED {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_ALREADY_ENDED);
        }
    },
    RECRUITING {
        @Override
        public void validate() {

        }
    };

    public abstract void validate();
}
