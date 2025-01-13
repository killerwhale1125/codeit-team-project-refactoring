package com.gathering.gathering.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageGatheringsCountResponse {
    private long participatingCount;
    private long completedCount;
    private long myCreatedCount;
    private long myWishedCount;

    public static MyPageGatheringsCountResponse fromEntity(long participatingCount, long completedCount, long myCreatedCount, long myWishedCount) {
        return MyPageGatheringsCountResponse.builder()
                .participatingCount(participatingCount)
                .completedCount(completedCount)
                .myCreatedCount(myCreatedCount)
                .myWishedCount(myWishedCount)
                .build();
    }
}
