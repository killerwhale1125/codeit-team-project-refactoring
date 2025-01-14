package com.gathering.gathering.infrastructure;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringReviewSortType;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import com.gathering.gathering.service.port.GatheringSearchRepository;
import com.gathering.gathering.util.GatheringSearchConditionBuilder;
import com.gathering.gathering.util.GatheringSortUtil;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.dto.GatheringReviewDto;
import com.gathering.review.model.dto.ReviewListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gathering.book.model.entity.QBook.book;
import static com.gathering.challenge.model.entity.QChallenge.challenge;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;
import static com.gathering.gatheringuser.infrastructure.entity.QGatheringUser.gatheringUser;
import static com.gathering.image.model.entity.QImage.image;
import static com.gathering.review.model.entitiy.QGatheringReview.gatheringReview;
import static com.gathering.user.model.entitiy.QUser.user;


@Repository
@RequiredArgsConstructor
public class GatheringSearchRepositoryImpl implements GatheringSearchRepository {

    private final JPAQueryFactory queryFactory;
    private final GatheringSearchConditionBuilder gatheringSearchConditionBuilder;
    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;

    // 무한스크롤 전용
    @Override
    public GatheringSliceResponse findGatherings(GatheringSearch gatheringSearch, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 조건 생성
        BooleanBuilder builder = gatheringSearchConditionBuilder.buildConditionAll(gatheringSearch);
        // Query 생성
        JPAQuery<Gathering> query = queryFactory.selectFrom(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .leftJoin(gathering.image, image).fetchJoin()
                .where(builder);

        // 정렬 처리
        GatheringSortUtil.applySorting(query, gatheringSearch.getGatheringSortType());

        /**
         * 페이징 처리
         * LIMIT + 1을 통해 다음 데이터가 있는지 없는지 여부 판단
         */
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<GatheringDomain> contents = query.fetch().stream()
                .map(Gathering::toEntity)
                .collect(Collectors.toList());

        /**
         * hasNext true -> 요청한 size 외에 데이터가 더 존재함
         * hasNext false -> 더이상 다음에 조회할 데이터가 존재하지 않음
         */
        boolean hasNext = contents.size() > pageable.getPageSize();

        // pageable.getPageSize() + 1개 중 마지막 하나를 제거
        if (hasNext) {
            contents.remove(contents.size() - 1);
        }
        
        // 꼭 필요할 때만 count 쿼리 실행
        SliceImpl<GatheringDomain> slice = new SliceImpl<>(contents, pageable, hasNext);

        return GatheringSliceResponse.builder()
                .gatherings(slice.getContent())
                .hasNext(hasNext)
                .build();
    }

    @Override
    public Slice<Gathering> findJoinableGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        // 조건 생성
        BooleanBuilder builder = gatheringSearchConditionBuilder.buildConditionAll(gatheringSearch);
        // Query 생성
        JPAQuery<Gathering> query = queryFactory.selectFrom(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .where(builder);

        // 정렬 처리
        GatheringSortUtil.applySorting(query, gatheringSearch.getGatheringSortType());

        /**
         * 페이징 처리
         * LIMIT + 1을 통해 다음 데이터가 있는지 없는지 여부 판단
         */
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<Gathering> contents = query.fetch();

        /**
         * hasNext true -> 요청한 size 외에 데이터가 더 존재함
         * hasNext false -> 더이상 다음에 조회할 데이터가 존재하지 않음
         */
        boolean hasNext = contents.size() > pageable.getPageSize();

        // pageable.getPageSize() + 1개 중 마지막 하나를 제거
        if (hasNext) {
            contents.remove(contents.size() - 1);
        }

        // 꼭 필요할 때만 count 쿼리 실행
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Optional<Gathering> getGatheringWithChallengeAndBook(Long gatheringId) {
        return Optional.empty();
    }

    @Override
    public Page<Gathering> findGatheringsForUserByUsername(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        BooleanBuilder builder = gatheringSearchConditionBuilder.buildGatheringAndUserStatus(gatheringStatus, gatheringUserStatus);

        JPAQuery<Gathering> query = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .where(gatheringUser.user.userName.eq(username).and(builder));

        List<Gathering> result = query.offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .where(gatheringUser.user.userName.eq(username).and(builder))
                .fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }

    // 내가 만든 모임
    @Override
    public Page<Gathering> findMyCreated(String username, Pageable pageable) {
        List<Gathering> result = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .where(gathering.owner.eq(username))
                .offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .where(gathering.owner.eq(username))
                .fetchCount();
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<Gathering> findMyWishes(Set<Long> wishGatheringIds, Pageable pageable) {
        List<Gathering> result = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .where(gathering.id.in(wishGatheringIds))
                .offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .where(gathering.id.in(wishGatheringIds))
                .fetchCount();

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public ReviewListDto getGatheringReviewList(Long gatheringId, GatheringReviewSortType sort, Pageable pageable) {
        // 리뷰 목록
        JPAQuery<GatheringReviewDto> query = queryFactory
                .select(Projections.constructor(GatheringReviewDto.class,
                        gatheringReview.id,
                        user.id,
                        user.userName,
                        user.profile,
                        gatheringReview.score,
                        gatheringReview.content,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", gatheringReview.createdTime)))
                .from(gatheringReview)
                .leftJoin(user).on(user.id.eq(gatheringReview.user.id))
                .where(gatheringReview.gathering.id.eq(gatheringId).and(gatheringReview.status.eq(StatusType.Y)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        // 정렬 처리
        GatheringSortUtil.applyReviewSorting(query, sort);

        List<GatheringReviewDto> reviews = query.fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        // Remove the extra record from the content (if it exists)
        if (hasNext) {
            reviews.remove(reviews.size() - 1);
        }

        Tuple result = queryFactory
                .select(
                        gatheringReview.id.count(), // COUNT
                        gatheringReview.score.avg()// AVG
                        ,JPAExpressions
                                .select(gatheringReview.count())
                                .from(gatheringReview)
                                .where(
                                        gatheringReview.gathering.id.eq(gatheringId)
                                                .and(gatheringReview.score.between(4,5))
                                                .and(gatheringReview.status.eq(StatusType.Y))
                                )
                        ,JPAExpressions
                                .select(gatheringReview.count())
                                .from(gatheringReview)
                                .where(
                                        gatheringReview.gathering.id.eq(gatheringId)
                                                .and(gatheringReview.score.eq(3))
                                                .and(gatheringReview.status.eq(StatusType.Y))
                                )
                        ,JPAExpressions
                                .select(gatheringReview.count())
                                .from(gatheringReview)
                                .where(
                                        gatheringReview.gathering.id.eq(gatheringId)
                                                .and(gatheringReview.score.between(1,2))
                                                .and(gatheringReview.status.eq(StatusType.Y))
                                )
                )
                .from(gatheringReview)
                .where(
                        gatheringReview.gathering.id.eq(gatheringId)
                                .and(gatheringReview.status.eq(StatusType.Y))
                )
                .fetchOne();

        return ReviewListDto.fromGatheringReviews(reviews, result, hasNext);
    }

}
