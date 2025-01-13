package com.gathering.gathering.model.domain;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.challenge.model.entity.ChallengeStatus.COMPLETED;
import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.model.entity.GatheringStatus.FULL;
import static com.gathering.gathering.model.entity.GatheringStatus.RECRUITING;
import static com.gathering.gathering.model.entity.GatheringUserStatus.*;
import static com.gathering.gathering.model.entity.GatheringWeek.ONE_WEEK;
import static com.gathering.gathering.model.entity.ReadingTimeGoal.ONE_HOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GatheringDomainTest {

    @Test
    @DisplayName("GatheringCreate와 챌린지, 책, 이미지, 사용자 정보로 모임을 생성할 수 있다.")
    void create() {
        /* given */

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);

        final UserDomain user = UserDomain.builder()
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        final BookDomain book = BookDomain.builder()
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

        final ChallengeDomain challenge = ChallengeDomain.builder()
                .challengeStatus(INACTIVE)
                .completeRate(0.0)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(ONE_HOUR)
                .build();

        final ImageDomain image = ImageDomain.builder()
                .name("이미지 이름")
                .url("이미지 url")
                .removed(false)
                .build();

        /* when */
        final GatheringDomain gathering = GatheringDomain.create(gatheringCreate, challenge, book, List.of(image), user, new TestGatheringValidator());

        /* then */
        // 모임 ( Gathering )
        assertThat(gathering.getName()).isEqualTo("모임 제목");
        assertThat(gathering.getStartDate()).isEqualTo(startDate);
        assertThat(gathering.getEndDate()).isEqualTo(endDate);
        assertThat(gathering.getMinCapacity()).isEqualTo(10);
        assertThat(gathering.getMaxCapacity()).isEqualTo(20);
        assertThat(gathering.getGatheringStatus()).isEqualTo(RECRUITING);
        assertThat(gathering.getGatheringWeek()).isEqualTo(ONE_WEEK);
        assertThat(gathering.getCurrentCapacity()).isEqualTo(1);
        assertThat(gathering.getOwner()).isEqualTo("범고래1");
        assertThat(gathering.getViewCount()).isEqualTo(0);

        // 책 ( Book )
        assertThat(gathering.getBook().getTitle()).isEqualTo("책 제목");

        // 챌린지 ( Challenge )
        assertThat(gathering.getChallenge().getChallengeStatus()).isEqualTo(INACTIVE);
        assertThat(gathering.getChallenge().getCompleteRate()).isEqualTo(0.0);
        assertThat(gathering.getChallenge().getStartDate()).isEqualTo(startDate);
        assertThat(gathering.getChallenge().getEndDate()).isEqualTo(endDate);
        assertThat(gathering.getChallenge().getReadingTimeGoal()).isEqualTo(ONE_HOUR);

        // 이미지 ( Image )
        assertThat(gathering.getImage().getName()).isEqualTo("이미지 이름");
    }

    @Test
    @DisplayName("사용자는 조건이 부합할 경우 모임에 참여할 수 있다.")
    void join() {
        /* given */
        final LocalDate now = LocalDate.now().plusDays(1);
        final LocalDate endDate = now.plusDays(10);
        GatheringDomain gathering = createGathering(now, endDate);
        final GatheringValidator gatheringValidator = new TestGatheringValidator();

        final UserDomain user = UserDomain.builder()
                .id(2L)
                .userName("범고래2")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        /* when */
        GatheringDomain.join(gathering, user, gatheringValidator);

        /* then */
        assertThat(gathering.getCurrentCapacity()).isEqualTo(2);
    }

    @Test
    @DisplayName("모임 떠나기")
    void leave() {
        /* given */
        LocalDate now = LocalDate.now().plusDays(1);
        LocalDate endDate = now.plusDays(10);
        GatheringDomain gathering = createGathering(now, endDate);
        GatheringValidator gatheringValidator = new TestGatheringValidator();
        final UserDomain user = UserDomain.builder()
                .id(2L)
                .userName("범고래2")
                .password("Password1!")
                .email("killerwhale2@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        final GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                .id(2L)
                .user(user)
                .gathering(gathering)
                .gatheringUserStatus(PARTICIPATING)
                .build();
        GatheringDomain.join(gathering, user, gatheringValidator);
        gathering.getGatheringUsers().add(gatheringUser);

        /* when */
        GatheringDomain.leave(gathering, user);

        /* then */
        assertThat(gathering.getCurrentCapacity()).isEqualTo(1);
        assertThat(gathering.getGatheringUsers()).hasSize(1);
    }

    @Test
    @DisplayName("모임 종료")
    void end() {
        /* given */
        final LocalDate now = LocalDate.now();
        final LocalDate endDate = now.plusDays(10);
        GatheringDomain gathering = createGathering(now, endDate);

        /* when */
        GatheringDomain.end(gathering);

        /* then */
        assertThat(gathering.getGatheringStatus().name()).isEqualTo(COMPLETED.name());
    }

    private GatheringDomain createGathering(LocalDate startDate, LocalDate endDate) {
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);

        final UserDomain user = UserDomain.builder()
                .id(1L)
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        final BookDomain book = BookDomain.builder()
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

        final ChallengeDomain challenge = ChallengeDomain.builder()
                .challengeStatus(INACTIVE)
                .completeRate(0.0)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(ONE_HOUR)
                .build();

        final ImageDomain image = ImageDomain.builder()
                .name("이미지 이름")
                .url("이미지 url")
                .removed(false)
                .build();

        final GatheringValidator gatheringValidator = new TestGatheringValidator();

        GatheringDomain gathering = GatheringDomain.create(gatheringCreate, challenge, book, List.of(image), user, gatheringValidator);
        GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                .id(1L)
                .user(user)
                .gathering(gathering)
                .gatheringUserStatus(PARTICIPATING)
                .build();
        gathering.getGatheringUsers().add(gatheringUser);
        return gathering;
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