package com.gathering.gatheringuser.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.infrastructure.entity.GatheringUserStatus;
import com.gathering.user.model.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.gathering.gathering.infrastructure.entity.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.gathering.infrastructure.entity.GatheringUserStatus.PARTICIPATING;

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

    public static List<GatheringUserDomain> end(List<GatheringUserDomain> gatheringUsers) {
        for(GatheringUserDomain gatheringUser : gatheringUsers) {
            gatheringUser.gatheringUserStatus = NOT_PARTICIPATING;
        }
        return gatheringUsers;
    }
}
