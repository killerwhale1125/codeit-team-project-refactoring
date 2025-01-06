package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringUserStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GatheringRequest {
    private GatheringUserStatus gatheringUserStatus;
}
