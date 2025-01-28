package com.gathering.gathering.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.dto.GatheringPageResponse;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import com.gathering.gathering.service.port.GatheringSearchRepository;
import com.gathering.gathering.util.GatheringSearchConditionBuilder;
import com.gathering.gathering.util.GatheringSortUtil;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gathering.book.infrastructure.entity.QBook.book;
import static com.gathering.challenge.infrastructure.entity.QChallenge.challenge;
import static com.gathering.gathering.domain.GatheringStatus.COMPLETED;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;
import static com.gathering.gathering_user.infrastructure.entity.QGatheringUser.gatheringUser;
import static com.gathering.image.infrastructure.entity.QImage.image;
import static com.gathering.user.infrastructure.entitiy.QUser.user;


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
                .join(gathering.challenge, challenge).fetchJoin()
                .join(gathering.book, book).fetchJoin()
                .join(gathering.image, image).fetchJoin()
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
    public GatheringSliceResponse findJoinableGatherings(GatheringSearch gatheringSearch, int page, int size) {
        // 조건 생성
        BooleanBuilder builder = gatheringSearchConditionBuilder.buildConditionAll(gatheringSearch);
        Pageable pageable = PageRequest.of(page, size);
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
    public GatheringDomain getByIdWithChallengeAndBook(Long gatheringId) {
        return gatheringSearchJpaRepository.getByIdWithChallengeAndBook(gatheringId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_GATHERING))
                .toEntity();
    }

    @Override
    public GatheringPageResponse findGatheringsBySearchWordAndTypeTitle(String searchWord, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> gatherings = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeTitle(searchWord, pageable);
        return GatheringPageResponse.builder()
                .objects(gatherings.getContent())
                .totalCount(gatherings.getTotalElements())
                .build();
    }

    @Override
    public GatheringPageResponse findGatheringsBySearchWordAndTypeContent(String searchWord, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> gatherings = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeContent(searchWord, pageable);
        return GatheringPageResponse.builder()
                .objects(gatherings.getContent())
                .totalCount(gatherings.getTotalElements())
                .build();
    }

    @Override
    public GatheringPageResponse findGatheringsBySearchWordAndTypeBookName(String searchWord, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> gatherings = gatheringSearchJpaRepository.findGatheringsBySearchWordAndTypeBookName(searchWord, pageable);
        return GatheringPageResponse.builder()
                .objects(gatherings.getContent())
                .totalCount(gatherings.getTotalElements())
                .build();
    }

    @Override
    public List<Long> findCompletedGatheringBookIdsByUserId(Long userId) {
        return gatheringSearchJpaRepository.findCompletedGatheringBookIdsByUserId(userId, COMPLETED);
    }

    @Override
    public GatheringPageResponse findGatheringsForUserByUsername(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus) {
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = gatheringSearchConditionBuilder.buildGatheringAndUserStatus(gatheringStatus, gatheringUserStatus);

        JPAQuery<Gathering> query = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .where(gatheringUser.user.userName.eq(username).and(builder));

        List<GatheringDomain> result = query.offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch().stream().map(Gathering::toEntity).collect(Collectors.toList());

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .where(gatheringUser.user.userName.eq(username).and(builder))
                .fetchCount();

        Page<GatheringDomain> gatherings = new PageImpl<>(result, pageable, totalCount);

        return GatheringPageResponse.builder()
                .gatherings(gatherings.getContent())
                .totalCount(totalCount)
                .build();

    }

    // 내가 만든 모임
    @Override
    public GatheringPageResponse findMyCreated(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<GatheringDomain> result = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .where(gathering.owner.eq(username))
                .offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch().stream().map(Gathering::toEntity).collect(Collectors.toList());

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .where(gathering.owner.eq(username))
                .fetchCount();

        Page<GatheringDomain> gatherings = new PageImpl<>(result, pageable, totalCount);

        return GatheringPageResponse.builder()
                .gatherings(gatherings.getContent())
                .totalCount(totalCount)
                .build();
    }

    @Override
    public GatheringPageResponse findMyWishes(Set<Long> wishGatheringIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<GatheringDomain> result = queryFactory.select(gathering)
                .from(gathering)
                .leftJoin(gathering.challenge, challenge).fetchJoin()
                .leftJoin(gathering.book, book).fetchJoin()
                .leftJoin(gathering.gatheringUsers, gatheringUser).fetchJoin()
                .leftJoin(gatheringUser.user, user).fetchJoin()
                .where(gathering.id.in(wishGatheringIds))
                .offset(pageable.getOffset())  // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch().stream().map(Gathering::toEntity).collect(Collectors.toList());

        long totalCount = queryFactory.select(gathering.id)
                .from(gathering)
                .where(gathering.id.in(wishGatheringIds))
                .fetchCount();

        Page<GatheringDomain> gatherings = new PageImpl<>(result, pageable, totalCount);

        return GatheringPageResponse.builder()
                .gatherings(gatherings.getContent())
                .totalCount(totalCount)
                .build();
    }

}
