package com.gathering.gathering.util;

import com.gathering.gathering.controller.response.MyPageGatheringsCountResponse;
import com.gathering.gathering.service.port.GatheringRepository;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GatheringActions {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    public MyPageGatheringsCountResponse getMyPageGatheringsCount(UserDomain user) {

        long participatingCount = gatheringRepository.getActiveAndParticipatingCount(user.getId());
        long completedCount = gatheringRepository.getCompletedCount(user.getId());
        long myCreatedCount = gatheringRepository.getMyCreatedCount(user.getUserName());

        Set<Long> wishGatheringIds = userRepository.findWishGatheringIdsByUserName(user.getUserName());
        long myWishedCount = gatheringRepository.getMyWishedCountByGatheringIds(wishGatheringIds);

        return MyPageGatheringsCountResponse.fromEntity(participatingCount, completedCount, myCreatedCount, myWishedCount);
    }

    public void readBookGathering(Long gatheringId, String username) {

    }

}
