package com.gathering.gathering.util;

import com.gathering.book.repository.BookRepository;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.image.model.entity.Image;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.model.entitiy.UserAttendance;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import com.gathering.user.repository.UserAttendanceBookJpaRepository;
import com.gathering.user.repository.UserAttendanceJpaRepository;
import com.gathering.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringActions {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final GatheringValidator gatheringValidator;
    private final BookRepository bookRepository;
    private final UserAttendanceJpaRepository userAttendanceRepository;
    private final UserAttendanceBookJpaRepository userAttendanceBookRepository;

    public Gathering createGathering(GatheringCreate gatheringCreate, String username, List<MultipartFile> files) {
//        List<Image> images = gatheringImageService.uploadGatheringImage(files);
//        UserDomain user = userRepository.findByUsername(username);
//        Book book = bookRepository.findById(gatheringCreate.getBookId());
//        book.incrementSelectedCount();
//
//        return Gathering.createGathering(
//                gatheringCreate,
//                Challenge.createChallenge(gatheringCreate, ChallengeUser.createChallengeUser(user)),
//                book,
//                images,
//                GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING),
//                gatheringValidator);
        return null;
    }


    public void joinGathering(Long gatheringId, String username) {
        UserDomain user = userRepository.findByUsername(username);
        Gathering gathering = gatheringRepository.getGatheringAndGatheringUsersById(gatheringId);

        Gathering.join(gathering, user, GatheringUser.createGatheringUser(user, GatheringUserStatus.PARTICIPATING), gatheringValidator);
        Challenge.join(gathering.getChallenge(), ChallengeUser.createChallengeUser(user));
    }

    public void leaveGathering(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus) {
        Gathering gathering = gatheringRepository.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus);
        UserDomain user = userRepository.findByUsername(username);

        Gathering.leave(gathering, user);
        Challenge challenge = challengeRepository.getChallengeUsersById(gathering.getChallenge().getId());
        Challenge.leave(challenge, user);
    }

    public Gathering deleteGathering(Long gatheringId, String username) {
        UserDomain user = userRepository.findByUsername(username);
        Gathering gathering = gatheringRepository.getById(gatheringId);
        gatheringValidator.validateOwner(gathering.getOwner(), user.getUserName());
        return gathering;
    }

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
        UserDomain user = userRepository.findByUsername(username);
        Gathering gathering = gatheringRepository.getGatheringAndGatheringUsersById(gatheringId);
        LocalDate today = LocalDate.now();

        // 오늘 날짜의 출석 정보를 조회한다.
        Optional<UserAttendance> attendance =
                userAttendanceRepository.findByUserIdAndCreateDate(user.getId(), today);

        // 모임을 통하여 오늘 날짜 출석에 독서 기록이 있는지 조회한다.
        Optional<UserAttendanceBook> attendanceBook = userAttendanceBookRepository.findByUserAttendanceAndGathering(attendance.get(), gathering);

        /**
         * 모임에 해당하는 출석의 독서 기록이 없으면 해당 모임의 독서 기록을 생성한다.
         * 독서기록이 있다면 중복 기록은 불가능하므로 예외를 발생시킨다.
         */
        if(attendanceBook.isEmpty()) {
            userAttendanceBookRepository.save(UserAttendanceBook.createUserAttendanceBook(attendance.get(), gathering.getBook(), gathering));
        } else {
            throw new BaseException(BaseResponseStatus.ALREADY_READING_BOOK);
        }

        // 챌린지 달성률 증가
        long userAttendanceCount = userAttendanceRepository.getUserAttendancesByUserIdAndDate(user.getId(), gathering.getStartDate(), gathering.getEndDate()).stream()
                .filter(userAttendance -> userAttendance.getUserAttendanceBooks().contains(gathering.getId()))
                .count();

        // 유저가 참여하고있는 모임의 챌린지 유저 정보를 가져온다.
        ChallengeUser challengeUser = challengeRepository.getChallengeUserByChallengeIdAndUserId(gathering.getChallenge().getId(), user.getId());

        // 개개인의 독서 달성률을 새로 갱신시킨다.
        challengeUser.updateReadingRate(gathering.getGatheringWeek(), userAttendanceCount);
    }

}
