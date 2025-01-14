package com.gathering.gathering.service;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.gathering.controller.port.GatheringSearchService;
import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.*;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.mock.fake.repository.*;
import com.gathering.mock.fake.util.FakeGatheringAsync;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.mock.test.TestPrincipalDetails;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.domain.GatheringSortType.DEADLINE_ASC;
import static com.gathering.gathering.domain.GatheringStatus.*;
import static com.gathering.gathering.domain.GatheringWeek.*;
import static com.gathering.gathering.domain.ReadingTimeGoal.*;
import static com.gathering.gatheringuser.domain.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.gatheringuser.domain.GatheringUserStatus.PARTICIPATING;
import static org.assertj.core.api.Assertions.assertThat;

class GatheringSearchServiceImplTest {
    private GatheringSearchService gatheringSearchService;

    @BeforeEach
    void setUp() {
        FakeGatheringSearchRepository fakeGatheringSearchRepository = new FakeGatheringSearchRepository();
        FakeGatheringAsync fakeGatheringAsync = new FakeGatheringAsync();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeReviewRepository fakeReviewRepository = new FakeReviewRepository();
        FakeChallengeRepository fakeChallengeRepository = new FakeChallengeRepository();
        FakeBookRepository fakeBookRepository = new FakeBookRepository();
        FakeGatheringRepository fakeGatheringRepository = new FakeGatheringRepository();
        FakeChallengeUserRepository fakeChallengeUserRepository = new FakeChallengeUserRepository();
        FakeGatheringUserRepository fakeGatheringUserRepository = new FakeGatheringUserRepository();
        this.gatheringSearchService = GatheringSearchServiceImpl.builder()
                .gatheringSearchRepository(fakeGatheringSearchRepository)
                .gatheringAsync(fakeGatheringAsync)
                .userRepository(fakeUserRepository)
                .reviewRepository(fakeReviewRepository)
                .challengeRepository(fakeChallengeRepository)
                .build();
        
        /* 테스트 사용자 생성 */
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

        /* 테스트 책 생성 */
        final BookDomain book = BookDomain.builder()
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

        fakeBookRepository.saveBook(book);

        final ImageDomain image = ImageDomain.builder()
                .name("이미지 이름")
                .url("이미지 url")
                .removed(false)
                .build();
        final TestGatheringValidator testGatheringValidator = new TestGatheringValidator();

        /* 테스트 모임 생성 */
        createTestGatherings().stream().forEach(createGathering -> {
            /* 챌린지 */
            final ChallengeDomain createChallenge = ChallengeDomain.builder()
                    .challengeStatus(INACTIVE)
                    .completeRate(0.0)
                    .startDate(createGathering.getStartDate())
                    .endDate(createGathering.getEndDate())
                    .readingTimeGoal(createGathering.getReadingTimeGoal())
                    .build();
            final ChallengeDomain challenge = fakeChallengeRepository.save(createChallenge);

            /* 챌린지 유저 */
            final ChallengeUserDomain createChallengeUser = ChallengeUserDomain.builder()
                    .challenge(challenge)
                    .attendanceDate(null)
                    .attendanceRate(0.0)
                    .readingRate(0.0)
                    .user(user1)
                    .build();
            fakeChallengeUserRepository.save(createChallengeUser);

            /* 모임 */
            final GatheringDomain saveGathering = GatheringDomain.create(createGathering, challenge, book, List.of(image), user1, testGatheringValidator);
            final GatheringDomain gathering = fakeGatheringSearchRepository.save(saveGathering);

            GatheringUserStatus gatheringUserStatus = null;
            switch(gathering.getGatheringStatus()) {
                case DELETED -> gatheringUserStatus = NOT_PARTICIPATING;
                case COMPLETED -> gatheringUserStatus = NOT_PARTICIPATING;
                default -> gatheringUserStatus = PARTICIPATING;
            }

            /* 모임 유저 */
            final GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                    .user(user1)
                    .gathering(gathering)
                    .gatheringUserStatus(gatheringUserStatus)
                    .build();

            fakeGatheringUserRepository.save(gatheringUser);
        });
    }

    @Test
    @DisplayName("모집중(RECRUITING), 목표 기간 1주(ONE_WEEK), 목표 독서시간 1시간(ONE_HOUR), 시작일 <= 종료일 안에 포함된 모임 목록을 조회한다.")
    void findRecruitingGatheringsByFilters() {
        /* given */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(ONE_WEEK.getWeek() + 2);
        GatheringSearch gatheringSearch = getGatheringSearch(startDate, endDate, DEADLINE_ASC, "책 제목", RECRUITING, List.of(ONE_HOUR), false);
        int page = 0;
        int size = 5;
        UserDetails userDetails = new TestPrincipalDetails(getUser());

        /* when */
        GatheringSearchResponse result = gatheringSearchService.findGatheringsByFilters(gatheringSearch, page, size, userDetails);

        /* then */
        assertThat(result.isHasNext()).isFalse();
        List<GatheringResponse> gatheringResponses = result.getGatheringResponses();
        assertThat(result.isHasNext()).isFalse();
        assertThat(gatheringResponses).hasSize(1);
        assertThat(gatheringResponses.get(0).getId()).isEqualTo(1L);
        assertThat(gatheringResponses.get(0).getOwner()).isEqualTo(userDetails.getUsername());
        assertThat(gatheringResponses.get(0).getName()).isEqualTo("모임1");
        assertThat(gatheringResponses.get(0).getContent()).isEqualTo("모임장 소개1");
        assertThat(gatheringResponses.get(0).getGatheringWeek()).isEqualTo(ONE_WEEK.getWeek());
        assertThat(gatheringResponses.get(0).getReadingTimeGoal()).isEqualTo(ONE_HOUR.getMinutes());
        assertThat(gatheringResponses.get(0).getStartDate()).isAfterOrEqualTo(startDate);
        assertThat(gatheringResponses.get(0).getEndDate()).isBeforeOrEqualTo(endDate);
        assertThat(gatheringResponses.get(0).getMinCapacity()).isEqualTo(10);
        assertThat(gatheringResponses.get(0).getMaxCapacity()).isEqualTo(20);
        assertThat(gatheringResponses.get(0).getCurrentCapacity()).isEqualTo(1);
        assertThat(gatheringResponses.get(0).getGatheringStatus()).isEqualTo(RECRUITING);
        assertThat(gatheringResponses.get(0).getBookTitle()).isEqualTo("책 제목");
        assertThat(gatheringResponses.get(0).getBookImage()).isEqualTo("http://localhost:8080/book-image");
        assertThat(gatheringResponses.get(0).getPublisher()).isEqualTo("출판사");
        assertThat(gatheringResponses.get(0).getPublishDate()).isEqualTo("2023-01-25");
        assertThat(gatheringResponses.get(0).getStar()).isEqualTo(3.5);
        assertThat(gatheringResponses.get(0).getAuthor()).isEqualTo("책 저자");
        assertThat(gatheringResponses.get(0).isWish()).isFalse();
    }

    @Test
    @DisplayName("활동중(ACTIVE), 목표 기간 2주(TWO_WEEKS), 목표 독서시간 1시간(ONE_HOUR), 시작일 <= 종료일 안에 포함된 모임 목록을 조회한다.")
    void findActiveGatheringsByFilters() {
        /* given */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(TWO_WEEKS.getWeek() + 2);
        GatheringSearch gatheringSearch = getGatheringSearch(startDate, endDate, DEADLINE_ASC, "책 제목", ACTIVE, List.of(ONE_HOUR), false);
        int page = 0;
        int size = 5;
        UserDetails userDetails = new TestPrincipalDetails(getUser());

        /* when */
        GatheringSearchResponse result = gatheringSearchService.findGatheringsByFilters(gatheringSearch, page, size, userDetails);

        /* then */
        List<GatheringResponse> gatheringResponses = result.getGatheringResponses();
        assertThat(result.isHasNext()).isFalse();
        assertThat(gatheringResponses).hasSize(1);
        assertThat(gatheringResponses.get(0).getId()).isEqualTo(2L);
        assertThat(gatheringResponses.get(0).getOwner()).isEqualTo(userDetails.getUsername());
        assertThat(gatheringResponses.get(0).getName()).isEqualTo("모임2");
        assertThat(gatheringResponses.get(0).getContent()).isEqualTo("모임장 소개2");
        assertThat(gatheringResponses.get(0).getGatheringWeek()).isEqualTo(TWO_WEEKS.getWeek());
        assertThat(gatheringResponses.get(0).getReadingTimeGoal()).isEqualTo(ONE_HOUR.getMinutes());
        assertThat(gatheringResponses.get(0).getStartDate()).isAfterOrEqualTo(startDate);
        assertThat(gatheringResponses.get(0).getEndDate()).isBeforeOrEqualTo(endDate);
        assertThat(gatheringResponses.get(0).getMinCapacity()).isEqualTo(10);
        assertThat(gatheringResponses.get(0).getMaxCapacity()).isEqualTo(20);
        assertThat(gatheringResponses.get(0).getCurrentCapacity()).isEqualTo(1);
        assertThat(gatheringResponses.get(0).getGatheringStatus()).isEqualTo(ACTIVE);
        assertThat(gatheringResponses.get(0).getBookTitle()).isEqualTo("책 제목");
        assertThat(gatheringResponses.get(0).getBookImage()).isEqualTo("http://localhost:8080/book-image");
        assertThat(gatheringResponses.get(0).getPublisher()).isEqualTo("출판사");
        assertThat(gatheringResponses.get(0).getPublishDate()).isEqualTo("2023-01-25");
        assertThat(gatheringResponses.get(0).getStar()).isEqualTo(3.5);
        assertThat(gatheringResponses.get(0).getAuthor()).isEqualTo("책 저자");
        assertThat(gatheringResponses.get(0).isWish()).isFalse();
    }

    @Test
    @DisplayName("활동중(FULL), 목표 기간 3주(THREE_WEEKS), 목표 독서시간 30분(THIRTY_MINUTES), 시작일 <= 종료일 안에 포함된 모임 목록을 조회한다.")
    void findFullGatheringsByFilters() {
        /* given */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(THREE_WEEKS.getWeek() + 2);
        GatheringSearch gatheringSearch = getGatheringSearch(startDate, endDate, DEADLINE_ASC, "책 제목", FULL, List.of(THIRTY_MINUTES), false);
        int page = 0;
        int size = 5;
        UserDetails userDetails = new TestPrincipalDetails(getUser());

        /* when */
        GatheringSearchResponse result = gatheringSearchService.findGatheringsByFilters(gatheringSearch, page, size, userDetails);

        /* then */
        List<GatheringResponse> gatheringResponses = result.getGatheringResponses();
        assertThat(result.isHasNext()).isFalse();
        assertThat(gatheringResponses).hasSize(1);
        assertThat(gatheringResponses.get(0).getId()).isEqualTo(3L);
        assertThat(gatheringResponses.get(0).getOwner()).isEqualTo(userDetails.getUsername());
        assertThat(gatheringResponses.get(0).getName()).isEqualTo("모임3");
        assertThat(gatheringResponses.get(0).getContent()).isEqualTo("모임장 소개3");
        assertThat(gatheringResponses.get(0).getGatheringWeek()).isEqualTo(THREE_WEEKS.getWeek());
        assertThat(gatheringResponses.get(0).getReadingTimeGoal()).isEqualTo(THIRTY_MINUTES.getMinutes());
        assertThat(gatheringResponses.get(0).getStartDate()).isAfterOrEqualTo(startDate);
        assertThat(gatheringResponses.get(0).getEndDate()).isBeforeOrEqualTo(endDate);
        assertThat(gatheringResponses.get(0).getMinCapacity()).isEqualTo(10);
        assertThat(gatheringResponses.get(0).getMaxCapacity()).isEqualTo(20);
        assertThat(gatheringResponses.get(0).getCurrentCapacity()).isEqualTo(1);
        assertThat(gatheringResponses.get(0).getGatheringStatus()).isEqualTo(FULL);
        assertThat(gatheringResponses.get(0).getBookTitle()).isEqualTo("책 제목");
        assertThat(gatheringResponses.get(0).getBookImage()).isEqualTo("http://localhost:8080/book-image");
        assertThat(gatheringResponses.get(0).getPublisher()).isEqualTo("출판사");
        assertThat(gatheringResponses.get(0).getPublishDate()).isEqualTo("2023-01-25");
        assertThat(gatheringResponses.get(0).getStar()).isEqualTo(3.5);
        assertThat(gatheringResponses.get(0).getAuthor()).isEqualTo("책 저자");
        assertThat(gatheringResponses.get(0).isWish()).isFalse();
    }

    @Test
    @DisplayName("완료됨(COMPLETED), 목표 기간 4주(FOUR_WEEKS), 목표 독서시간 10분(TEN_MINUTES), 시작일 <= 종료일 안에 포함된 모임 목록을 조회한다.")
    void findCompletedGatheringsByFilters() {
        /* given */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(FOUR_WEEKS.getWeek() + 2);
        GatheringSearch gatheringSearch = getGatheringSearch(startDate, endDate, DEADLINE_ASC, "책 제목", COMPLETED, List.of(TEN_MINUTES), false);
        int page = 0;
        int size = 5;
        UserDetails userDetails = new TestPrincipalDetails(getUser());

        /* when */
        GatheringSearchResponse result = gatheringSearchService.findGatheringsByFilters(gatheringSearch, page, size, userDetails);

        /* then */
        List<GatheringResponse> gatheringResponses = result.getGatheringResponses();
        assertThat(result.isHasNext()).isFalse();
        assertThat(gatheringResponses).hasSize(1);
        assertThat(gatheringResponses.get(0).getId()).isEqualTo(4L);
        assertThat(gatheringResponses.get(0).getOwner()).isEqualTo(userDetails.getUsername());
        assertThat(gatheringResponses.get(0).getName()).isEqualTo("모임4");
        assertThat(gatheringResponses.get(0).getContent()).isEqualTo("모임장 소개4");
        assertThat(gatheringResponses.get(0).getGatheringWeek()).isEqualTo(FOUR_WEEKS.getWeek());
        assertThat(gatheringResponses.get(0).getReadingTimeGoal()).isEqualTo(TEN_MINUTES.getMinutes());
        assertThat(gatheringResponses.get(0).getStartDate()).isAfterOrEqualTo(startDate);
        assertThat(gatheringResponses.get(0).getEndDate()).isBeforeOrEqualTo(endDate);
        assertThat(gatheringResponses.get(0).getMinCapacity()).isEqualTo(10);
        assertThat(gatheringResponses.get(0).getMaxCapacity()).isEqualTo(20);
        assertThat(gatheringResponses.get(0).getCurrentCapacity()).isEqualTo(1);
        assertThat(gatheringResponses.get(0).getGatheringStatus()).isEqualTo(COMPLETED);
        assertThat(gatheringResponses.get(0).getBookTitle()).isEqualTo("책 제목");
        assertThat(gatheringResponses.get(0).getBookImage()).isEqualTo("http://localhost:8080/book-image");
        assertThat(gatheringResponses.get(0).getPublisher()).isEqualTo("출판사");
        assertThat(gatheringResponses.get(0).getPublishDate()).isEqualTo("2023-01-25");
        assertThat(gatheringResponses.get(0).getStar()).isEqualTo(3.5);
        assertThat(gatheringResponses.get(0).getAuthor()).isEqualTo("책 저자");
        assertThat(gatheringResponses.get(0).isWish()).isFalse();
    }


    private List<GatheringCreate> createTestGatherings() {
        final LocalDate startDate1 = LocalDate.now().plusDays(1);
        final LocalDate endDate1 = startDate1.plusDays(ONE_WEEK.getWeek());
        final GatheringCreate gatheringCreate1 =
                getGatheringCreate("모임1", "모임장 소개1", startDate1, endDate1, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);

        final LocalDate startDate2 = LocalDate.now().plusDays(1);
        final LocalDate endDate2 = startDate1.plusDays(TWO_WEEKS.getWeek());
        final GatheringCreate gatheringCreate2 =
                getGatheringCreate("모임2", "모임장 소개2", startDate2, endDate2, 10, 20, 2L, ACTIVE, ONE_HOUR, TWO_WEEKS);

        final LocalDate startDate3 = LocalDate.now().plusDays(1);
        final LocalDate endDate3 = startDate1.plusDays(THREE_WEEKS.getWeek());
        final GatheringCreate gatheringCreate3 =
                getGatheringCreate("모임3", "모임장 소개3", startDate3, endDate3, 10, 20, 2L, FULL, THIRTY_MINUTES, THREE_WEEKS);

        final LocalDate startDate4 = LocalDate.now().plusDays(1);
        final LocalDate endDate4 = startDate1.plusDays(FOUR_WEEKS.getWeek());
        final GatheringCreate gatheringCreate4 =
                getGatheringCreate("모임4", "모임장 소개4", startDate4, endDate4, 10, 20, 2L, COMPLETED, TEN_MINUTES, FOUR_WEEKS);

        return List.of(gatheringCreate1, gatheringCreate2, gatheringCreate3, gatheringCreate4);
    }

    private static UserDomain getUser() {
        UserDomain user = UserDomain.builder()
                .id(1L)
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .wishGatheringIds(new HashSet<>())
                .build();
        return user;
    }

    private static GatheringSearch getGatheringSearch(LocalDate startDate,
                                                      LocalDate endDate,
                                                      GatheringSortType gatheringSortType,
                                                      String bookTitle,
                                                      GatheringStatus gatheringStatus,
                                                      List<ReadingTimeGoal> readingTimeGoals,
                                                      Boolean today) {
        return GatheringSearch.builder()
                .gatheringSortType(gatheringSortType)
                .bookTitle(bookTitle)
                .startDate(startDate)
                .endDate(endDate)
                .gatheringStatus(gatheringStatus)
                .readingTimeGoals(readingTimeGoals)
                .today(today)
                .build();
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