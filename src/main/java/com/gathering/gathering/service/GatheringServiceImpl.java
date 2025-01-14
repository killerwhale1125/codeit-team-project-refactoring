package com.gathering.gathering.service;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.book.repository.BookRepository;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.repository.ChallengeUserRepository;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.controller.port.GatheringService;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringCreate;
import com.gathering.gathering.controller.response.MyPageGatheringsCountResponse;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering.service.port.GatheringRepository;
import com.gathering.gathering.util.GatheringActions;
import com.gathering.gathering.util.GatheringValidator;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.gatheringuser.service.port.GatheringUserRepository;
import com.gathering.gatheringuser.util.GatheringUserDtoMapper;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.service.ImageService;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.repository.UserRepository;
import com.gathering.user_attendance.model.domain.UserAttendanceDomain;
import com.gathering.user_attendance.repository.UserAttendanceRepository;
import com.gathering.user_attendance_book.model.domain.UserAttendanceBookDomain;
import com.gathering.user_attendance_book.repository.UserAttendanceBookRepository;
import com.gathering.util.string.UUIDUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Service
@Builder
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {

    private final GatheringRepository gatheringRepository;
    private final GatheringUserRepository gatheringUserRepository;
    private final UserRepository userRepository;
    private final GatheringActions gatheringActions;
    private final BookRepository bookRepository;
    private final ChallengeUserRepository challengeUserRepository;
    private final ChallengeRepository challengeRepository;
    private final UserAttendanceRepository userAttendanceRepository;
    private final UserAttendanceBookRepository userAttendanceBookRepository;
    private final ImageService imageService;
    private final GatheringValidator gatheringValidator;
    private final UUIDUtils uuidUtils;

    @Override
    @Transactional
    public GatheringDomain create(GatheringCreate gatheringCreate, List<MultipartFile> files, String username) {
        UserDomain user = userRepository.findByUsername(username);
        ChallengeDomain challenge = createChallengeForUserAndSave(gatheringCreate, user);
        BookDomain book = bookRepository.findById(gatheringCreate.getBookId());
        book.incrementSelectedCount();
        bookRepository.updateSelectedCount(book);
        List<ImageDomain> images = imageService.uploadImage(files, EntityType.GATHERING, uuidUtils);
        return createGatheringAndSave(gatheringCreate, user, challenge, book, images, gatheringValidator);
    }

    @Override
    @Transactional
    public void join(Long gatheringId, String userName) {
        UserDomain user = userRepository.findByUsername(userName);

        GatheringDomain gathering = gatheringRepository.findByIdWithGatheringUsersAndChallenge(gatheringId);
        GatheringDomain.join(gathering, user, gatheringValidator);

        gatheringRepository.join(gathering);
        gatheringUserRepository.save(GatheringUserDomain.create(user, gathering));
        challengeUserRepository.save(ChallengeUserDomain.create(user, gathering.getChallenge()));
    }

    @Override
    @Transactional
    public void leave(Long gatheringId, String username) {
        GatheringDomain gathering = gatheringRepository.findByIdWithGatheringUsersAndChallenge(gatheringId);
        UserDomain user = userRepository.findByUsername(username);

        GatheringUserDomain gatheringUser = GatheringDomain.leave(gathering, user);

        gatheringRepository.leave(gathering);
        gatheringUserRepository.deleteById(gatheringUser.getId());

        ChallengeDomain challenge = challengeRepository.getByIdWithChallengeUsers(gathering.getChallenge().getId());
        ChallengeUserDomain challengeUser = ChallengeDomain.leave(challenge, user);
        challengeUserRepository.deleteById(challengeUser.getId());
    }

    @Override
    @Transactional
    public void delete(Long gatheringId, String userName) {
        UserDomain user = userRepository.findByUsername(userName);
        GatheringDomain gathering = gatheringRepository.getById(gatheringId);
        gatheringValidator.validateOwner(gathering.getOwner(), user.getUserName());
        gatheringRepository.deleteById(gathering.getId());
    }

    @Override
    @Transactional
    public void wish(Long gatheringId, String username) {
        UserDomain user = userRepository.findByUsername(username);
        GatheringDomain gathering = gatheringRepository.getById(gatheringId);
        UserDomain.wishGathering(user, gathering.getId());
        userRepository.save(user);
    }

    @Override
    public LocalDate calculateEndDate(LocalDate startDate, GatheringWeek gatheringWeek) {
        return startDate.plusDays(gatheringWeek.getWeek());
    }

    @Override
    public MyPageGatheringsCountResponse getMyPageGatheringsCount(String username) {
        UserDomain user = userRepository.findByUsername(username);

        long participatingCount = gatheringRepository.getActiveAndParticipatingCount(user.getId());
        long completedCount = gatheringRepository.getCompletedCount(user.getId());
        long myCreatedCount = gatheringRepository.getMyCreatedCount(user.getUserName());

        Set<Long> wishGatheringIds = userRepository.findWishGatheringIdsByUserName(user.getUserName());
        long myWishedCount = wishGatheringIds.size();

        return MyPageGatheringsCountResponse.fromEntity(participatingCount, completedCount, myCreatedCount, myWishedCount);
    }

    @Override
    @Transactional
    public void end(Long gatheringId) {
        GatheringDomain gathering = gatheringRepository.findByIdWithGatheringUsersAndChallenge(gatheringId);
        challengeRepository.save(ChallengeDomain.end(gathering.getChallenge()));
        gatheringRepository.save(GatheringDomain.end(gathering));
        gatheringUserRepository.saveAll(GatheringUserDomain.end(gathering.getGatheringUsers()));
    }

    @Override
    @Transactional
    public UserAttendanceBookDomain readBookCompleted(String username, Long gatheringId) {
        UserDomain user = userRepository.findByUsername(username);
        GatheringDomain gathering = gatheringRepository.findByIdWithBookAndChallenge(gatheringId);
        // 오늘 날짜의 출석 정보를 조회한다.
        UserAttendanceDomain userAttendance =
                userAttendanceRepository.findByUserIdAndCreateDateWithAttendanceBooksAndGathering(user.getId(), LocalDate.now());

        // 모임을 통하여 오늘 날짜 출석에 독서 기록이 있는지 조회한다.
        boolean isUserAttendanceBook = userAttendance.getUserAttendanceBooks().stream()
                .anyMatch(userAttendanceBook -> userAttendanceBook.getGathering().getId() == gathering.getId());

        /**
         * 모임에 해당하는 출석의 독서 기록이 없으면 해당 모임의 독서 기록을 생성한다.
         * 독서기록이 있다면 중복 기록은 불가능하므로 예외를 발생시킨다.
         */
        UserAttendanceBookDomain userAttendanceBook = null;
        if(isUserAttendanceBook) {
            throw new BaseException(ALREADY_READING_BOOK);
        } else {
            userAttendanceBook = userAttendanceBookRepository.save(UserAttendanceBookDomain.create(userAttendance, gathering.getBook(), gathering));
        }

        // 모임 기간 사이의 읽은 책의 출석 갯수 조회
        long userAttendanceCount = userAttendanceBookRepository.getByUserIdAndBetweenDate(user.getId(), gathering.getStartDate(), gathering.getEndDate(), gathering.getBook().getId());

        // 유저가 참여하고있는 모임의 챌린지 유저 정보를 가져온다.
        ChallengeUserDomain challengeUser = challengeUserRepository.getByChallengeIdAndUserId(gathering.getChallenge().getId(), user.getId());

        // 개개인의 독서 달성률을 새로 갱신시킨다. ( +1 은 save 되어도 바로 DB에 반영이 되지 않기 때문에 미리 카운트를 증가시키기 위함 )
        challengeUser.updateReadingRate(gathering.getGatheringWeek(), ++userAttendanceCount);
        challengeUserRepository.readBookCompleted(challengeUser);
        return userAttendanceBook;
    }

    @Override
    public List<UserResponseDto> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus) {
        List<GatheringUserDomain> gatheringUser = gatheringUserRepository.findByGatheringIdAndStatusWithUsers(gatheringId, gatheringUserStatus);
        return GatheringUserDtoMapper.mapToUserResponseDtos(gatheringUser);
    }

    private GatheringDomain createGatheringAndSave(GatheringCreate gatheringCreate, UserDomain user, ChallengeDomain challenge, BookDomain book, List<ImageDomain> images, GatheringValidator gatheringValidator) {
        GatheringDomain gathering = gatheringRepository.save(
                GatheringDomain.create(
                        gatheringCreate,
                        challenge,
                        book,
                        images,
                        user,
                        gatheringValidator)
        );

        gatheringUserRepository.save(GatheringUserDomain.create(user, gathering));
        return gathering;
    }

    private ChallengeDomain createChallengeForUserAndSave(GatheringCreate gatheringCreate, UserDomain user) {
        ChallengeDomain challenge = challengeRepository.save(ChallengeDomain.create(gatheringCreate));
        challengeUserRepository.save(ChallengeUserDomain.create(user, challenge));
        return challenge;
    }

}
