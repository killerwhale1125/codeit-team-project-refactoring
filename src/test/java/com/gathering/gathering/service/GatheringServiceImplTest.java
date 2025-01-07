package com.gathering.gathering.service;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.gatheringuser.repository.GatheringUserRepository;
import com.gathering.mock.fake.repository.*;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.mock.fake.service.FakeAwsS3Service;
import com.gathering.mock.fake.service.FakeImageService;
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
import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("모임을 생성하면 챌린지도 생성된다.")
    void createGathering() {
        /* given */
        final String username = "범고래1";

        final LocalDate startDate = LocalDate.of(2025, 01, 10);
        final LocalDate endDate = LocalDate.of(2025, 02, 10);

        final GatheringCreate gatheringCreate = GatheringCreate.builder()
                .name("모임 제목")
                .content("모임장 소개")
                .startDate(startDate)
                .endDate(endDate)
                .minCapacity(10)
                .maxCapacity(20)
                .bookId(1L)
                .gatheringStatus(RECRUITING)
                .readingTimeGoal(ONE_HOUR)
                .gatheringWeek(ONE_WEEK)
                .build();

        final TestMultipartFile file = new TestMultipartFile(
                "file",
                "thumbnail-image.jpg",
                "image/jpeg",
                "image content".getBytes()
        );

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
}