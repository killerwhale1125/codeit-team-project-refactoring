package com.gathering.gathering.domain;

import com.gathering.common.base.exception.BaseException;

import static com.gathering.common.base.response.BaseResponseStatus.*;

public enum GatheringStatus {
    // 인원 꽉참
    FULL {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_FULL);
        }
    },
    // 모임 활동 중
    ACTIVE {
        @Override
        public void validate() {

        }
    },
    // 삭제된 모임
    DELETED {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_ALREADY_DELETED);
        }
    },
    // 완료된 모임
    COMPLETED {
        @Override
        public void validate() {
            throw new BaseException(GATHERING_ALREADY_ENDED);
        }
    },
    // 모집 중
    RECRUITING {
        @Override
        public void validate() {

        }
    };

    public abstract void validate();
}
