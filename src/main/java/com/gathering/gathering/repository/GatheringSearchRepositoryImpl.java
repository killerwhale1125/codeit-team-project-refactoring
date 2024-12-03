package com.gathering.gathering.repository;

import com.gathering.category.model.entity.QCategory;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.category.model.entity.QCategory.*;
import static com.gathering.challenge.model.entity.QChallenge.challenge;
import static com.gathering.gathering.model.entity.QGathering.gathering;
import static io.micrometer.common.util.StringUtils.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Repository
public class GatheringSearchRepositoryImpl implements GatheringSearchRepository {

    private final JPAQueryFactory queryFactory;

    public GatheringSearchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Gathering> findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        BooleanBuilder conditions = buildConditions(gatheringSearch);

        JPAQuery<Gathering> query = queryFactory.selectFrom(gathering)
                .join(gathering.challenge, challenge)
                .where(conditions);

//        applySorting(query, gatheringSearch.getGatheringSortType());
        return null;
    }

//    private void applySorting(JPAQuery<Gathering> query, GatheringSortType gatheringSortType) {
//        if (gatheringSortType != null) {
//            switch (gatheringSortType) {
//                case DEADLINE_ASC:
//                    query.orderBy(gathering.endDateTime.asc());
//                    break;
//                case PARTICIPANTS_DESC:
//                    query.orderBy(gathering.challenge.challengeUsers.size().desc());
//                    break;
//                case VIEWS_DESC:
//                    query.orderBy(gathering.vi.desc());
//                    break;
//                case NEWEST_FIRST:
//                    query.orderBy(gathering.createDate.desc());
//                    break;
//            }
//        }
//        return query;
//    }

    private BooleanBuilder buildConditions(GatheringSearch gatheringSearch) {
        BooleanBuilder builder = new BooleanBuilder();

        // 각 필터 조건을 메서드로 분리하여 관리
//        builder.and(categoryCondition(gatheringSearch.getCategoryId()));
        builder.and(bookTitleCondition(gatheringSearch.getBookTitle()));
        builder.and(stateCondition(gatheringSearch.getState()));
        builder.and(cityCondition(gatheringSearch.getCity()));
        builder.and(townCondition(gatheringSearch.getTown()));
        builder.and(startDateCondition(gatheringSearch.getStartDate()));
        builder.and(endDateCondition(gatheringSearch.getEndDate()));
        builder.and(goalTimeCondition(gatheringSearch.getGoalTime()));
        builder.and(gatheringStatusCondition(gatheringSearch.getGatheringStatus()));

        return builder;
    }

    // 개별 조건을 메서드로 분리
//    private BooleanExpression categoryCondition(long categoryId) {
//        return categoryId > 0 ? gathering.book.category.id.eq(categoryId) : null;
//    }

    private BooleanExpression bookTitleCondition(String bookTitle) {
        return isNotEmpty(bookTitle) ? gathering.book.title.containsIgnoreCase(bookTitle) : null;
    }

    private BooleanExpression stateCondition(String state) {
        return isNotEmpty(state) ? gathering.gatheringAddress.state.eq(state) : null;
    }

    private BooleanExpression cityCondition(String city) {
        return isNotEmpty(city) ? gathering.gatheringAddress.city.eq(city) : null;
    }

    private BooleanExpression townCondition(String town) {
        return isNotEmpty(town) ? gathering.gatheringAddress.town.eq(town) : null;
    }

    private BooleanExpression startDateCondition(LocalDate startDate) {
        return startDate != null ? gathering.challenge.startDateTime.goe(startDate) : null;
    }

    private BooleanExpression endDateCondition(LocalDate endDate) {
        return endDate != null ? gathering.challenge.endDateTime.loe(endDate) : null;
    }

    private BooleanExpression goalTimeCondition(long goalTime) {
        return goalTime > 0 ? gathering.challenge.goalTime.eq(goalTime) : null;
    }

    private BooleanExpression gatheringStatusCondition(GatheringStatus status) {
        return status != null ? gathering.gatheringStatus.eq(status) : null;
    }
}
