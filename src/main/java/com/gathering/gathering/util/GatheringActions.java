package com.gathering.gathering.util;

import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringActions {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    public List<UserResponseDto> mapToUserResponseDtos(List<GatheringUser> gatheringUsers) {
        return gatheringUsers.stream()
                .map(gatheringUser -> UserResponseDto.fromEntity(gatheringUser.getUser()))
                .collect(Collectors.toList());
    }

    public MyPageGatheringsCountResponse getMyPageGatheringsCount(UserDomain user) {

        long participatingCount = gatheringRepository.getActiveAndParticipatingCount(user.getId());
        long completedCount = gatheringRepository.getCompletedCount(user.getId());
        long myCreatedCount = gatheringRepository.getMyCreatedCount(user.getUserName());

        Set<Long> wishGatheringIds = userRepository.findWishGatheringIdsByUserName(user.getUserName());
        long myWishedCount = gatheringRepository.getMyWishedCountByGatheringIds(wishGatheringIds);

        return MyPageGatheringsCountResponse.fromEntity(participatingCount, completedCount, myCreatedCount, myWishedCount);
    }

    public void readBookGathering(Long gatheringId, String username) {
//        UserDomain user = userRepository.findByUsername(username);
//        Gathering gathering = gatheringRepository.getGatheringAndGatheringUsersById(gatheringId);
//        LocalDate today = LocalDate.now();
//
//        // 오늘 날짜의 출석 정보를 조회한다.
//        Optional<UserAttendance> attendance =
//                userAttendanceRepository.findByUserIdAndCreateDate(user.getId(), today);
//
//        // 모임을 통하여 오늘 날짜 출석에 독서 기록이 있는지 조회한다.
//        Optional<UserAttendanceBook> attendanceBook = userAttendanceBookRepository.findByUserAttendanceAndGathering(attendance.get(), gathering);
//
//        /**
//         * 모임에 해당하는 출석의 독서 기록이 없으면 해당 모임의 독서 기록을 생성한다.
//         * 독서기록이 있다면 중복 기록은 불가능하므로 예외를 발생시킨다.
//         */
//        if(attendanceBook.isEmpty()) {
//            userAttendanceBookRepository.save(UserAttendanceBook.createUserAttendanceBook(attendance.get(), gathering.getBook(), gathering));
//        } else {
//            throw new BaseException(BaseResponseStatus.ALREADY_READING_BOOK);
//        }
//
//        // 챌린지 달성률 증가
//        long userAttendanceCount = userAttendanceRepository.getUserAttendancesByUserIdAndDate(user.getId(), gathering.getStartDate(), gathering.getEndDate()).stream()
//                .filter(userAttendance -> userAttendance.getUserAttendanceBooks().contains(gathering.getId()))
//                .count();
//
//        // 유저가 참여하고있는 모임의 챌린지 유저 정보를 가져온다.
//        ChallengeUser challengeUser = challengeRepository.getChallengeUserByChallengeIdAndUserId(gathering.getChallenge().getId(), user.getId());
//
//        // 개개인의 독서 달성률을 새로 갱신시킨다.
//        challengeUser.updateReadingRate(gathering.getGatheringWeek(), userAttendanceCount);
    }

}
