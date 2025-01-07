package com.gathering.gatheringuser.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.user.model.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import static com.gathering.gathering.model.entity.GatheringUserStatus.PARTICIPATING;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringUserDomain {
    private Long id;
    private GatheringDomain gathering;
    private UserDomain user;
    private GatheringUserStatus gatheringUserStatus;

    public static GatheringUserDomain create(UserDomain user, GatheringDomain gathering) {
        return GatheringUserDomain.builder()
                .user(user)
                .gathering(gathering)
                .gatheringUserStatus(PARTICIPATING)
                .build();
    }
}
