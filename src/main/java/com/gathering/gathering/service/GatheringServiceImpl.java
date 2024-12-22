package com.gathering.gathering.service;

import com.gathering.challenge.redis.ChallengeRedisService;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringUpdate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gathering.util.GatheringActions;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final GatheringActions gatheringActions;
    private final ChallengeRedisService challengeRedisService;

    @Override
    @Transactional
    public void create(GatheringCreate gatheringCreate, List<MultipartFile> files, String username) {
        Gathering gathering = gatheringActions.createGathering(gatheringCreate, username, files);
        gatheringRepository.save(gathering);
        // 현재 시간에서 모임 시작일까지 시간 간격을 계산하여 그 시간을 만료설정으로 레디스에 저장하여 Pub/Sub 이벤트 발생시켜 추후 Challenge 상태변화
        challengeRedisService.scheduleChallengeStateChange(gathering.getChallenge(), gathering.getStartDate());
    }

    @Override
    @Transactional
    public void join(Long gatheringId, String userName) {
        gatheringActions.joinGathering(gatheringId, userName);
    }

    @Override
    @Transactional
    public void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus) {
        gatheringActions.leaveGathering(gatheringId, username, gatheringUserStatus);
    }

    @Override
    @Transactional
    public void wish(Long gatheringId, String username) {
        User user = userRepository.findByUsername(username);
        User.addWish(user, gatheringRepository.findIdById(gatheringId));
        userRepository.save(user);
    }

    @Override
    public void update(GatheringUpdate gatheringUpdate, String username) {

    }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate, GatheringWeek gatheringWeek) {
        return startDate.plusDays(gatheringWeek.getWeek());
    }

    @Override
    public MyPageGatheringsCountResponse getMyPageGatheringsCount(String username) {
        User user = userRepository.findByUsername(username);
        return gatheringActions.getMyPageGatheringsCount(user);
    }

    @Override
    @Transactional
    public void delete(Long gatheringId, String userName) {
        gatheringRepository.delete(gatheringActions.deleteGathering(gatheringId, userName));
    }

    @Override
    public List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        List<GatheringUser> gatheringUsers = gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus).getGatheringUsers();
        return gatheringActions.mapToUserResponseDtos(gatheringUsers);
    }

}
