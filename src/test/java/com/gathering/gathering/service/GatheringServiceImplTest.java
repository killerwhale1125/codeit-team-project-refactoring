package com.gathering.gathering.service;

import com.gathering.book.domain.BookDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.controller.port.GatheringService;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringCreate;
import com.gathering.gathering.controller.response.MyPageGatheringsCountResponse;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering.domain.ReadingTimeGoal;
import com.gathering.mock.fake.repository.*;
import com.gathering.mock.fake.service.FakeAwsS3Service;
import com.gathering.mock.fake.service.FakeImageService;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.mock.test.TestMultipartFile;
import com.gathering.mock.test.TestUUIDUtils;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.controller.response.UserResponseDto;
import com.gathering.user_attendance.domain.UserAttendanceDomain;
import com.gathering.user_attendance_book.domain.UserAttendanceBookDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.gathering.challenge.infrastructure.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.domain.GatheringStatus.RECRUITING;
import static com.gathering.gathering_user.domain.GatheringUserStatus.PARTICIPATING;
import static com.gathering.gathering.domain.GatheringWeek.ONE_WEEK;
import static com.gathering.gathering.domain.ReadingTimeGoal.ONE_HOUR;
import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GatheringServiceImplTest {

    private GatheringService gatheringService;

    @BeforeEach
    void setUp() {
        // 의존성 준비
        FakeGatheringRepository fakeGatheringRepository = new FakeGatheringRepository();
        FakeGatheringUserRepository fakeGatheringUserRepository = new FakeGatheringUserRepository();
        FakeBookRepository fakeBookRepository = new FakeBookRepository();
        FakeChallengeUserRepository fakeChallengeUserRepository = new FakeChallengeUserRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeChallengeRepository fakeChallengeRepository = new FakeChallengeRepository();
        FakeAwsS3Service fakeAwsS3Service = new FakeAwsS3Service();
        FakeImageRepository fakeImageRepository = new FakeImageRepository();
        FakeImageService fakeImageService = new FakeImageService(fakeAwsS3Service, fakeImageRepository);
        FakeUserAttendanceRepository fakeUserAttendanceRepository = new FakeUserAttendanceRepository();
        FakeUserAttendanceBookRepository fakeUserAttendanceBookRepository = new FakeUserAttendanceBookRepository();
        TestUUIDUtils testUUIDUtils = new TestUUIDUtils("randomUUID");
        TestGatheringValidator testGatheringValidator = new TestGatheringValidator();

        // 의존성 주입
        this.gatheringService = GatheringServiceImpl.builder()
                .gatheringRepository(fakeGatheringRepository)
                .gatheringUserRepository(fakeGatheringUserRepository)
                .bookRepository(fakeBookRepository)
                .challengeUserRepository(fakeChallengeUserRepository)
                .userRepository(fakeUserRepository)
                .challengeRepository(fakeChallengeRepository)
                .userAttendanceRepository(fakeUserAttendanceRepository)
                .userAttendanceBookRepository(fakeUserAttendanceBookRepository)
                .imageService(fakeImageService)
                .uuidUtils(testUUIDUtils)
                .gatheringValidator(testGatheringValidator)
                .build();

        
        // 테스트 유저 생성
        final UserDomain user1 = UserDomain.builder()
                .id(1L)
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .wishGatheringIds(new HashSet<>())
                .build();

        final UserDomain user2 = UserDomain.builder()
                .id(2L)
                .userName("범고래2")
                .password("Password1!")
                .email("killerwhale2@naver.com")
                .profile("userProfile")
                .roles("USER")
                .wishGatheringIds(new HashSet<>())
                .build();

        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);

        // 테스트 책 생성
        final BookDomain bookDomain = BookDomain.builder()
                .id(1L)
                .title("책 제목")
                .image("http://localhost:8080/book-image")
                .publisher("출판사")
                .author("책 저자")
                .publishDate("2023-01-25")
                .selectedCount(0L)
                .star(3.5)
                .introduce("책 소개")
                .totalPage(260)
                .build();

        fakeBookRepository.saveBook(bookDomain);

        UserAttendanceDomain userAttendance = UserAttendanceDomain.builder()
                .id(1L)
                .user(user1)
                .createDate(Date.valueOf(LocalDate.now()))
                .userAttendanceBooks(new ArrayList<>())
                .build();
        fakeUserAttendanceRepository.save(userAttendance);
    }



    @Test
    @DisplayName("책, 썸네일 이미지, 유저 정보를 통하여 챌린지와 모임을 생성한다.")
    void createGathering() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when */
        final GatheringDomain gathering = gatheringService.create(gatheringCreate, List.of(file), username);

        /* then */

        // 모임 ( Gathering )
        assertThat(gathering.getId()).isNotNull();
        assertThat(gathering.getName()).isEqualTo("모임 제목");
        assertThat(gathering.getStartDate()).isEqualTo(startDate);
        assertThat(gathering.getEndDate()).isEqualTo(endDate);
        assertThat(gathering.getMinCapacity()).isEqualTo(10);
        assertThat(gathering.getMaxCapacity()).isEqualTo(20);
        assertThat(gathering.getGatheringStatus()).isEqualTo(RECRUITING);
        assertThat(gathering.getGatheringWeek()).isEqualTo(ONE_WEEK);
        assertThat(gathering.getCurrentCapacity()).isEqualTo(1);
        assertThat(gathering.getOwner()).isEqualTo(username);
        assertThat(gathering.getViewCount()).isEqualTo(0);

        // 모임 유저 ( GatheringUser )
        assertThat(gathering.getGatheringUsers().get(0).getId()).isNotNull();
        assertThat(gathering.getGatheringUsers().get(0).getGatheringUserStatus()).isEqualTo(PARTICIPATING);

        // 책 ( Book )
        assertThat(gathering.getBook().getId()).isNotNull();
        assertThat(gathering.getBook().getTitle()).isEqualTo("책 제목");

        // 챌린지 ( Challenge )
        assertThat(gathering.getChallenge().getChallengeStatus()).isEqualTo(INACTIVE);
        assertThat(gathering.getChallenge().getCompleteRate()).isEqualTo(0.0);
        assertThat(gathering.getChallenge().getStartDate()).isEqualTo(startDate);
        assertThat(gathering.getChallenge().getEndDate()).isEqualTo(endDate);
        assertThat(gathering.getChallenge().getReadingTimeGoal()).isEqualTo(ONE_HOUR);

        // 챌린지 유저 ( ChallengeUser )
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getId()).isNotNull();    // 양방향 관계가 잘 설정되었는지 확인
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getChallenge()).isNotNull(); // 양방향 관계가 잘 설정되었는지 확인
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getAttendanceDate()).isNull();
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getAttendanceRate()).isEqualTo(0.0);
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getReadingRate()).isEqualTo(0.0);

        // 이미지 ( Image )
        assertThat(gathering.getImage().getId()).isNotNull();
        assertThat(gathering.getImage().getUrl()).isEqualTo("http://www.codeit.com/image/gathering/randomUUID.jpg");
    }

    @Test
    @DisplayName("모임을 생성할 때 사용자가 인원 제한 없음을 선택한 경우에는 " +
            "최대 최소 인원 수를 MAX 값을 전달하여 모임의 최소 인원 수는 5, 최대 인원 수는 MAX 값이 저장된다.")
    void createGatheringWithUnlimitedCapacity() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, MAX_VALUE, MAX_VALUE, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when */
        final GatheringDomain gathering = gatheringService.create(gatheringCreate, List.of(file), username);

        /* then */

        // 모임 ( Gathering )
        assertThat(gathering.getId()).isNotNull();
        assertThat(gathering.getMinCapacity()).isEqualTo(5);
        assertThat(gathering.getMaxCapacity()).isEqualTo(MAX_VALUE);
    }

    @Test
    @DisplayName("시작일이 종료일보다 나중일 경우 예외가 발생한다.")
    void createGatheringWithValidateStartDate() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.minusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when then */
        // 모임 ( Gathering )
        assertThatThrownBy(() -> gatheringService.create(gatheringCreate, List.of(file), username))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("시작일과 종료일이 같은 날짜면 예외를 발생시킨다.")
    void createGatheringWithValidateEqualsDate() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate;
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when then */
        // 모임 ( Gathering )
        assertThatThrownBy(() -> gatheringService.create(gatheringCreate, List.of(file), username))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("시작일이 현재보다 이전 날짜일 경우 예외를 발생시킨다.")
    void createGatheringWithValidateStartDateNow() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now().minusDays(10);
        final LocalDate endDate = LocalDate.now().plusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when then */
        // 모임 ( Gathering )
        assertThatThrownBy(() -> gatheringService.create(gatheringCreate, List.of(file), username))
                .isInstanceOf(BaseException.class);
    }
    
    @Test
    @DisplayName("사용자는 아직 챌린지가 시작하지 않은 모임에 참여할 수 있다.")
    void join() {
        /* given */
        String owner = "범고래1";
        final LocalDate startDate = LocalDate.now().plusDays(1);
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 11, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        final Long gatheringId = gathering.getId();
        final String joinUsername = "범고래2";

        /* when */
        gatheringService.join(gatheringId, joinUsername);

        /* then */
        assertThat(gathering.getCurrentCapacity()).isEqualTo(2);
        assertThat(gathering.getGatheringUsers()).hasSize(2);
        assertThat(gathering.getChallenge().getChallengeUsers()).hasSize(2);
    }

    @Test
    @DisplayName("챌린지가 이미 시작했을 경우 모임에 참여할 수 없다.")
    void cannotJoinAlreadyStartedChallenge() {
        /* given */
        final String owner = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 11, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        final Long gatheringId = gathering.getId();
        final String joinUsername = "범고래2";
        /* when then */
        assertThatThrownBy(() -> gatheringService.join(gatheringId, joinUsername))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("이미 모임에 참여했을 경우 모임에 다시 참여할 수 없다.")
    void cannotJoinAlreadySJoinedUser() {
        /* given */
        String owner = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 11, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        final Long gatheringId = gathering.getId();
        final String joinUsername = "범고래1";
        /* when then */
        assertThatThrownBy(() -> gatheringService.join(gatheringId, joinUsername))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("모임 인원이 꽉찼을 경우 모임에 참여할 수 없다.")
    void cannotJoinFullGatheringStatus() {
        /* given */
        String owner = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 11, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        final Long gatheringId = gathering.getId();
        final String joinUsername = "범고래1";
        /* when then */
        assertThatThrownBy(() -> gatheringService.join(gatheringId, joinUsername))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("사용자는 아직 챌린지가 시작하지 않은 모임에 참여할 수 있다.")
    void leaveSuccess() {
        /* given */
        // 모임 생성
        String owner = "범고래1";
        final LocalDate startDate = LocalDate.now().plusDays(1);
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        // 모임 참여
        final Long gatheringId = gathering.getId();
        final String joinUsername = "범고래2";
        gatheringService.join(gatheringId, joinUsername);

        /* when */
        gatheringService.leave(gatheringId, joinUsername);

        /* then */
        assertThat(gathering.getCurrentCapacity()).isEqualTo(1);
        assertThat(gathering.getGatheringUsers()).hasSize(1);
        assertThat(gathering.getChallenge().getChallengeUsers()).hasSize(1);
    }

    @Test
    @DisplayName("모임장은 모임을 떠날 수 없다.")
    void ownerCannotLeave() {
        /* given */
        // 모임 생성
        String owner = "범고래1";
        final LocalDate startDate = LocalDate.now().plusDays(1);
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, owner);

        /* when then */
        assertThatThrownBy(() -> gatheringService.leave(gathering.getId(), owner))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("모임장이라면 모임을 삭제할 수 있다.")
    void deleteSuccess() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);

        /* when */
        gatheringService.delete(gathering.getId(), username);

        /* then */
        // Success
    }

    @Test
    @DisplayName("모임장이 아니라면 모임을 삭제할 수 없다.")
    void deleteFail() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);
        final String notOwnerUser = "범고래2";
        /* when then */
        assertThatThrownBy(() -> gatheringService.delete(gathering.getId(), notOwnerUser))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("참여 상태에 따라 모임에 참여중인 유저를 조회할 수 있다.")        
    void findGatheringIdAndStatusWithUsers() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);

        final GatheringUserStatus gatheringUserStatus = PARTICIPATING;

        /* when */
        final List<UserResponseDto> userResponses = gatheringService.findByGatheringIdAndStatusWithUsers(gathering.getId(), gatheringUserStatus);

        /* then */
        assertThat(userResponses).hasSize(1);
        assertThat(userResponses.get(0).getUsersId()).isEqualTo(1L);
        assertThat(userResponses.get(0).getUserName()).isEqualTo("범고래1");
        assertThat(userResponses.get(0).getEmail()).isEqualTo("killerwhale1125@naver.com");
        assertThat(userResponses.get(0).getProfile()).isEqualTo("userProfile");
        assertThat(userResponses.get(0).getRoles()).isEqualTo("USER");
    }

    @Test
    @DisplayName("사용자는 모임을 찜하지 않았다면 찜한 모임 리스트에 추가한다.")
    void wishAdd() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);

        /* when */
        gatheringService.wish(gathering.getId(), username);

        /* then */
        assertThat(gathering.getGatheringUsers().get(0).getUser().getWishGatheringIds()).hasSize(1);
    }

    @Test
    @DisplayName("사용자는 이미 찜한 모임을 또 찜한다면 찜 리스트에서 해당 모임을 삭제한다")
    void wishDelete() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);
        gatheringService.wish(gathering.getId(), username);

        /* when */
        gatheringService.wish(gathering.getId(), username);

        /* then */
        assertThat(gathering.getGatheringUsers().get(0).getUser().getWishGatheringIds()).hasSize(0);
    }

    @Test
    @DisplayName("모임(챌린지) 시작일과 진행일(주단위)로 모임(챌린지) 종료일을 계산한다.")
    void calculateEndDate() {
        /* given */
        final LocalDate startDate = LocalDate.now();
        final GatheringWeek gatheringWeek = ONE_WEEK;

        /* when */
        final LocalDate endDate = gatheringService.calculateEndDate(startDate, gatheringWeek);

        /* then */
        assertThat(endDate).isEqualTo(startDate.plusDays(gatheringWeek.getWeek()));
    }
    
    @Test
    @DisplayName("사용자 이름으로 마이페이지 모임 정보들의 갯수를 구한다.")
    void getMyPageGatheringsCount() {
        /* given */
        final String username1 = "범고래1";
        final String username2 = "범고래2";

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(ONE_WEEK.getWeek());

        final LocalDate startDate2 = LocalDate.now().plusDays(1);
        final LocalDate endDate2 = startDate2.plusDays(ONE_WEEK.getWeek());

        // 내가 만든 모임
        createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username1);

        // 참여중인 모임
        final GatheringDomain participatingGathering = createGathering(startDate2, endDate2, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username2);
        gatheringService.join(participatingGathering.getId(), username1);

        // 완료된 모임
        final GatheringDomain completedGathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username1);
        gatheringService.end(completedGathering.getId());

        // 내가 찜한 모임
        final GatheringDomain wishedGathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username2);
        gatheringService.wish(wishedGathering.getId(), username1);

        /* when */
        final MyPageGatheringsCountResponse myPageGatheringsCount = gatheringService.getMyPageGatheringsCount(username1);

        /* then */
        assertThat(myPageGatheringsCount.getMyCreatedCount()).isEqualTo(2L);
        assertThat(myPageGatheringsCount.getParticipatingCount()).isEqualTo(2L);
        assertThat(myPageGatheringsCount.getCompletedCount()).isEqualTo(1L);
        assertThat(myPageGatheringsCount.getMyWishedCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("독서 완료 버튼을 통하여 오늘 일자에 출석체크를 하고 챌린지 독서 달성률을 높일 수 있다.")
    void readBookCompleted() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(ONE_WEEK.getWeek());
        GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);

        /* when */
        UserAttendanceBookDomain userAttendanceBook = gatheringService.readBookCompleted(username, gathering.getId());

        /* then */
        assertThat(userAttendanceBook.getId()).isNotNull();
        assertThat(userAttendanceBook.getGathering().getId()).isEqualTo(gathering.getId());
        assertThat(gathering.getChallenge().getChallengeUsers().get(0).getReadingRate()).isGreaterThan(0);
    }

    @Test
    @DisplayName("이미 독서 읽기 완료했다면 중복으로 완료 요청을 할 수 없다.")
    void readBookFail() {
        /* given */
        final String username = "범고래1";
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(ONE_WEEK.getWeek());
        GatheringDomain gathering = createGathering(startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK, username);
        UserAttendanceBookDomain userAttendanceBook = gatheringService.readBookCompleted(username, gathering.getId());

        /* when then */
        assertThatThrownBy(() -> gatheringService.readBookCompleted(username, gathering.getId()))
                .isInstanceOf(BaseException.class);
    }

    public GatheringDomain createGathering(LocalDate startDate,
                                           LocalDate endDate,
                                           int minCapacity,
                                           int maxCapacity,
                                           Long bookId,
                                           GatheringStatus gatheringStatus,
                                           ReadingTimeGoal readingTimeGoal,
                                           GatheringWeek gatheringWeek,
                                           String username) {
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, minCapacity, maxCapacity, bookId, gatheringStatus, readingTimeGoal, gatheringWeek);
        final TestMultipartFile file = getTestMultipartFile();
        return gatheringService.create(gatheringCreate, List.of(file), username);
    }

    private static TestMultipartFile getTestMultipartFile() {
        final TestMultipartFile file = new TestMultipartFile(
                "file",
                "thumbnail-image.jpg",
                "image/jpeg",
                "image content".getBytes()
        );
        return file;
    }

    private static GatheringCreate getGatheringCreate(String name,
                                                      String content,
                                                      LocalDate startDate,
                                                      LocalDate endDate,
                                                      int minCapacity,
                                                      int maxCapacity,
                                                      long bookId,
                                                      GatheringStatus gatheringStatus,
                                                      ReadingTimeGoal readingTimeGoal,
                                                      GatheringWeek gatheringWeek) {
        return GatheringCreate.builder()
                .name(name)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .minCapacity(minCapacity)
                .maxCapacity(maxCapacity)
                .bookId(bookId)
                .gatheringStatus(gatheringStatus)
                .readingTimeGoal(readingTimeGoal)
                .gatheringWeek(gatheringWeek)
                .build();
    }
}