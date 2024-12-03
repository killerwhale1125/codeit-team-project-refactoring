package com.gathering.gathering.repository.search;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.search.util.GatheringSearchConditionBuilder;
import com.gathering.gathering.repository.search.util.GatheringSortUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.book.model.entity.QBook.book;
import static com.gathering.challenge.model.entity.QChallenge.challenge;
import static com.gathering.gathering.model.entity.QGathering.gathering;

@Repository
public class GatheringSearchRepositoryImpl implements GatheringSearchRepository {

    private final JPAQueryFactory queryFactory;

    public GatheringSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 무한스크롤 전용
    @Override
    public Slice<Gathering> findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        // 조건 생성
        BooleanBuilder builder = new GatheringSearchConditionBuilder(gatheringSearch).build();

        // Query 생성
        JPAQuery<Gathering> query = queryFactory.selectFrom(gathering)
                .leftJoin(gathering.challenge, challenge)
                .leftJoin(gathering.book, book).fetchJoin()
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

}
