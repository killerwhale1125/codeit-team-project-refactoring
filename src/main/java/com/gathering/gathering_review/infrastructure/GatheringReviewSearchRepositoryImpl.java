package com.gathering.gathering_review.infrastructure;

import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.controller.response.GatheringReviewsResponse;
import com.gathering.gathering_review.service.port.GatheringReviewSearchRepository;
import com.gathering.review.model.dto.GatheringReviewDto;
import com.gathering.user.domain.UserDomain;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.challenge.infrastructure.entity.QChallenge.challenge;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;
import static com.gathering.gathering_review.infrastructure.entity.QGatheringReview.gatheringReview;
import static com.gathering.gatheringuser.infrastructure.entity.QGatheringUser.gatheringUser;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class GatheringReviewSearchRepositoryImpl implements GatheringReviewSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GatheringReviewsResponse getMyReviews(UserDomain user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Long> list = findUnreviewedCompletedGatherings(user.getId());

        // 모임 response
        List<GatheringResponse> gatheringResponses = jpaQueryFactory
                .select(Projections.constructor(GatheringResponse.class,
                        gathering.id,
                        gathering.name,
                        gathering.startDate,
                        gathering.endDate,
                        challenge.readingTimeGoal,
                        gathering.image.url))
                .from(gathering)
                .leftJoin(gathering.challenge, challenge)
                .where(gathering.id.in(list))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<GatheringReviewResponse> reviews = jpaQueryFactory
                .select(Projections.constructor(GatheringReviewResponse.class,
                        gatheringReview.id,
                        gatheringReview.score,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", gatheringReview.createdTime),
                        gatheringReview.content))
                .from(gatheringReview)
                .where(gatheringReview.user.id.eq(user.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(gatheringReview.count())
                .from(gatheringReview)
                .where(gatheringReview.user.id.eq(user.getId()))
                .fetchOne();
        return GatheringReviewsResponse.fromGatheringReviews(gatheringResponses, list.size(), reviews, total);
    }

    private List<Long> findUnreviewedCompletedGatherings(long userId) {

        return jpaQueryFactory
                .select(gathering.id)
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser)
                .leftJoin(gathering.gatheringReviews, gatheringReview)
                .where(gatheringUserIdEq(userId),
                        gatheringStatusEq(GatheringStatus.COMPLETED),
                        gatheringReview.gathering.id.isNull())
                .fetch();
    }

    private BooleanExpression gatheringUserIdEq(long userId) {
        return isEmpty(userId) ? null : gatheringUser.user.id.eq(userId);
    }

    private BooleanExpression gatheringStatusEq(GatheringStatus gatheringStatus) {
        return isEmpty(gatheringStatus) ? null : gathering.gatheringStatus.eq(gatheringStatus);
    }
}
