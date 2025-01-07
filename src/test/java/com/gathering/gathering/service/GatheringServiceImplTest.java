package com.gathering.gathering.service;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import com.gathering.mock.fake.repository.*;
import com.gathering.mock.fake.service.FakeAwsS3Service;
import com.gathering.mock.fake.service.FakeImageService;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.mock.test.TestMultipartFile;
import com.gathering.mock.test.TestUUIDUtils;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.model.entity.GatheringStatus.RECRUITING;
import static com.gathering.gathering.model.entity.GatheringUserStatus.PARTICIPATING;
import static com.gathering.gathering.model.entity.GatheringWeek.ONE_WEEK;
import static com.gathering.gathering.model.entity.ReadingTimeGoal.ONE_HOUR;
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
                .imageService(fakeImageService)
                .uuidUtils(testUUIDUtils)
                .gatheringValidator(testGatheringValidator)
                .build();
        
        // 테스트 유저 생성
        UserDomain user1 = UserDomain.builder()
                .id(1L)
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        fakeUserRepository.save(user1);

        // 테스트 책 생성
        BookDomain bookDomain = BookDomain.builder()
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
        assertThat(gathering.getGatheringUsers().get(0).getGathering().getId()).isNotNull();    // 양방향 관계가 잘 설정되었는지 확인
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
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, MAX_VALUE, MAX_VALUE, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
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
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, MAX_VALUE, MAX_VALUE, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);
        final TestMultipartFile file = getTestMultipartFile();

        /* when then */
        // 모임 ( Gathering )
        assertThatThrownBy(() -> gatheringService.create(gatheringCreate, List.of(file), username))
                .isInstanceOf(BaseException.class);
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