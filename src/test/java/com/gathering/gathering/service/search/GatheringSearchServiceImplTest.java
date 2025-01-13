package com.gathering.gathering.service.search;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.*;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.mock.fake.repository.*;
import com.gathering.mock.fake.util.FakeGatheringAsync;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.mock.test.TestPrincipalDetails;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.model.entity.GatheringSortType.DEADLINE_ASC;
import static com.gathering.gathering.model.entity.GatheringStatus.*;
import static com.gathering.gathering.model.entity.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.gathering.model.entity.GatheringUserStatus.PARTICIPATING;
import static com.gathering.gathering.model.entity.GatheringWeek.*;
import static com.gathering.gathering.model.entity.ReadingTimeGoal.*;

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
        TestGatheringValidator testGatheringValidator = new TestGatheringValidator();

        /* 테스트 모임 생성 */
        createTestGatherings().stream().forEach(createGathering -> {
            /* 챌린지 */
            ChallengeDomain createChallenge = ChallengeDomain.builder()
                    .challengeStatus(INACTIVE)
                    .completeRate(0.0)
                    .startDate(createGathering.getStartDate())
                    .endDate(createGathering.getEndDate())
                    .readingTimeGoal(createGathering.getReadingTimeGoal())
                    .build();
            ChallengeDomain challenge = fakeChallengeRepository.save(createChallenge);

            /* 챌린지 유저 */
            ChallengeUserDomain createChallengeUser = ChallengeUserDomain.builder()
                    .challenge(challenge)
                    .attendanceDate(null)
                    .attendanceRate(0.0)
                    .readingRate(0.0)
                    .user(user1)
                    .build();
            fakeChallengeUserRepository.save(createChallengeUser);

            /* 모임 */
            GatheringDomain saveGathering = GatheringDomain.create(createGathering, challenge, book, List.of(image), user1, testGatheringValidator);
            GatheringDomain gathering = fakeGatheringRepository.save(saveGathering);

            GatheringUserStatus gatheringUserStatus = null;
            switch(gathering.getGatheringStatus()) {
                case DELETED -> gatheringUserStatus = NOT_PARTICIPATING;
                case COMPLETED -> gatheringUserStatus = NOT_PARTICIPATING;
                default -> gatheringUserStatus = PARTICIPATING;
            }

            /* 모임 유저 */
            GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                    .user(user1)
                    .gathering(gathering)
                    .gatheringUserStatus(gatheringUserStatus)
                    .build();

            fakeGatheringUserRepository.save(gatheringUser);
        });
    }

    private List<GatheringCreate> createTestGatherings() {
        final LocalDate startDate1 = LocalDate.now().plusDays(5);
        final LocalDate endDate1 = startDate1.plusDays(7);
        final GatheringCreate gatheringCreate1 =
                getGatheringCreate("모임1", "모임장 소개1", startDate1, endDate1, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);

        final LocalDate startDate2 = LocalDate.now().plusDays(5);
        final LocalDate endDate2 = startDate1.plusDays(14);
        final GatheringCreate gatheringCreate2 =
                getGatheringCreate("모임2", "모임장 소개2", startDate2, endDate2, 10, 20, 2L, ACTIVE, ONE_HOUR, TWO_WEEKS);

        final LocalDate startDate3 = LocalDate.now().plusDays(5);
        final LocalDate endDate3 = startDate1.plusDays(21);
        final GatheringCreate gatheringCreate3 =
                getGatheringCreate("모임3", "모임장 소개3", startDate3, endDate3, 10, 20, 2L, FULL, THIRTY_MINUTES, THREE_WEEKS);

        final LocalDate startDate4 = LocalDate.now().plusDays(5);
        final LocalDate endDate4 = startDate1.plusDays(28);
        final GatheringCreate gatheringCreate4 =
                getGatheringCreate("모임4", "모임장 소개4", startDate4, endDate4, 10, 20, 2L, COMPLETED, TEN_MINUTES, FOUR_WEEKS);

        return List.of(gatheringCreate1, gatheringCreate2, gatheringCreate3, gatheringCreate4);
    }

    @Test
    @DisplayName("GatheringSearch DTO 정보로 모임 목록을 조회한다. ( 무한스크롤 전용 )")
    void findGatheringsByFilters() {
        /* given */
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        GatheringSearch gatheringSearch = getGatheringSearch(startDate, endDate, DEADLINE_ASC, "책 제목", RECRUITING, List.of(ONE_HOUR, THIRTY_MINUTES), false);
        Pageable pageable = PageRequest.of(0, 5);
        UserDetails userDetails = new TestPrincipalDetails(getUser());

        /* when */
//        GatheringSearchResponse result = gatheringSearchService.findGatheringsByFilters(gatheringSearch, pageable, userDetails);

        /* then */

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