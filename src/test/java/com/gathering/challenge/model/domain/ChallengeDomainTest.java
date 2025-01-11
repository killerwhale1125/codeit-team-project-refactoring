package com.gathering.challenge.model.domain;

import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.gathering.challenge.model.entity.ChallengeStatus.INACTIVE;
import static com.gathering.gathering.model.entity.GatheringStatus.RECRUITING;
import static com.gathering.gathering.model.entity.GatheringWeek.ONE_WEEK;
import static com.gathering.gathering.model.entity.ReadingTimeGoal.ONE_HOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChallengeDomainTest {

    @Test
    @DisplayName("RequestBody로 챌린지를 생성한다.")
    void create() {
        /* given */
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final GatheringCreate gatheringCreate =
                getGatheringCreate("모임 제목", "모임장 소개", startDate, endDate, 10, 20, 1L, RECRUITING, ONE_HOUR, ONE_WEEK);

        /* when */
        final ChallengeDomain challenge = ChallengeDomain.create(gatheringCreate);
        /* then */
        assertThat(challenge.getChallengeStatus()).isEqualTo(INACTIVE);
        assertThat(challenge.getCompleteRate()).isEqualTo(0.0);
        assertThat(challenge.getStartDate()).isEqualTo(startDate);
        assertThat(challenge.getEndDate()).isEqualTo(endDate);
        assertThat(challenge.getReadingTimeGoal()).isEqualTo(gatheringCreate.getReadingTimeGoal());
    }

    @Test
    @DisplayName("챌린지를 떠나면 챌린지에서 유저가 제외된다.")
    void leaveSuccess() {
        /* given */
        final UserDomain user = UserDomain.builder()
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final ChallengeDomain challenge = ChallengeDomain.builder()
                .challengeUsers(new ArrayList<>())
                .challengeStatus(INACTIVE)
                .completeRate(0.0)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(ONE_HOUR)
                .build();

        challenge.getChallengeUsers().add(ChallengeUserDomain.create(user, challenge));

        /* when */
        ChallengeDomain.leave(challenge, user);

        /* then */
        assertThat(challenge.getChallengeUsers()).hasSize(0);
    }

    @Test
    @DisplayName("챌린지에 참여한 유저가 없을 때 챌린지에 나갈 경우 예외가 발생한다..")
    void leaveFail() {
        /* given */
        final UserDomain user = UserDomain.builder()
                .userName("범고래1")
                .password("Password1!")
                .email("killerwhale1125@naver.com")
                .profile("userProfile")
                .roles("USER")
                .build();

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(10);
        final ChallengeDomain challenge = ChallengeDomain.builder()
                .challengeUsers(new ArrayList<>())
                .challengeStatus(INACTIVE)
                .completeRate(0.0)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(ONE_HOUR)
                .build();

        /* when then */
        assertThatThrownBy(() -> ChallengeDomain.leave(challenge, user))
                .isInstanceOf(BaseException.class);
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