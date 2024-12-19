package com.gathering.review.repository;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.book.model.entity.Book;
import com.gathering.book.repository.BookJpaRepository;
import com.gathering.challenge.model.entity.ChallengeStatus;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringBookReview;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.gathering.repository.GatheringJpaRepository;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.dto.*;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import com.gathering.review.model.entitiy.QBookReview;
import com.gathering.review.model.entitiy.ReviewComment;
import com.gathering.review.util.ReviewQueryBuilder;
import com.gathering.user.model.entitiy.QUser;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.gathering.book.model.entity.QBook.book;
import static com.gathering.challenge.model.entity.QChallenge.challenge;
import static com.gathering.gathering.model.entity.QGathering.gathering;
import static com.gathering.gathering.model.entity.QGatheringBookReview.gatheringBookReview;
import static com.gathering.gathering.model.entity.QGatheringUser.gatheringUser;
import static com.gathering.review.model.dto.ReviewListDto.fromGatheringReviews;
import static com.gathering.review.model.entitiy.QBookReview.bookReview;
import static com.gathering.review.model.entitiy.QGatheringReview.gatheringReview;
import static com.gathering.review.model.entitiy.QReviewComment.reviewComment;
import static com.gathering.user.model.entitiy.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
@Transactional
public class ReviewRepositoryImpl implements ReviewRepository{

    private final BookReviewJpaRepository bookReviewJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final GatheringJpaRepository gatheringJpaRepository;
    private final ReviewCommentJpaRepository reviewCommentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final GatheringReviewJpaRepository gatheringReviewJpaRepository;
    private final ReviewQueryBuilder queryBuilder;

    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, ReviewType type) {

        User user = userJpaRepository.findByUserNameOrThrow(username);
        Gathering gathering = null;
        if(createReviewDto.getGatheringId() != 0) {
            gathering = gatheringJpaRepository.findById(createReviewDto.getGatheringId())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_GATHERING));
        }

        // 모임 리뷰
        if(type.equals(ReviewType.GATHERING)) {
            GatheringReview review = GatheringReview.createEntity(gathering, user, createReviewDto);

            review = gatheringReviewJpaRepository.save(review);

            return GatheringReviewDto.formEntity(review);
        } else {
            // 독서 리뷰

            Book book = bookJpaRepository.findById(createReviewDto.getBookId())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.BOOK_OR_CATEGORY_NOT_FOUND));


            BookReview review = BookReview.createEntity(book, user, createReviewDto);

            review = bookReviewJpaRepository.save(review);

            GatheringBookReview gatheringReview = GatheringBookReview.createGatheringReview(gathering, review);

            if(gathering != null) {
                gatheringReview.addGatheringReview(gathering);
            }
            return BookReviewDto.fromEntity(review);
        }

    }

    @Override
    public ReviewCommentDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {

        BookReview review = bookReviewJpaRepository.findByIdOrThrow(createReviewCommentDto.getReviewId());
        User user = userJpaRepository.findByUserNameOrThrow(username);

        // 댓글 순서 조회
        int orders = 0;
        long parent = createReviewCommentDto.getParent();
        if(parent != 0) {
            long count = reviewCommentJpaRepository.countByParent(parent);
            orders = (int) count + 1; // 대댓글 순서 계산
        } else {
            Long count = Optional.ofNullable(
                    jpaQueryFactory.select(reviewComment.count())
                            .from(reviewComment)
                            .where(reviewComment.review.id.eq(review.getId())
                                    .and(reviewComment.parent.eq(0L)))
                            .fetchOne()
            ).orElse(0L); // 결과가 null일 경우 기본값 0을 반환
            orders = (int) (count + 1); // 댓글 순서 계산
        }

        ReviewComment reviewComment = ReviewComment.createEntity(review, user, createReviewCommentDto, orders);

        reviewComment = reviewCommentJpaRepository.save(reviewComment);

        return ReviewCommentDto.formEntity(reviewComment);
    }

    @Override
    public ReviewListDto selectUserReviewList(String username, ReviewType type, Pageable pageable) {

        User user = userJpaRepository.findByUserNameOrThrow(username);
        ReviewListDto result = null;
        if(type.equals(ReviewType.BOOK)) {
            List<BookReviewDto> reviews = jpaQueryFactory
                    .select(Projections.constructor(BookReviewDto.class,
                            bookReview.id,
                            bookReview.title,
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime),
                            bookReview.content))
                    .from(bookReview)
                    .where(bookReview.user.id.eq(user.getId())
                            .and(bookReview.status.eq(StatusType.Y)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            Long total = jpaQueryFactory
                    .select(bookReview.count())
                    .from(bookReview)
                    .where(bookReview.user.id.eq(user.getId())
                            .and(bookReview.status.eq(StatusType.Y)))
                    .fetchOne();

            result = ReviewListDto.fromBookReviews(reviews,total);

            return result;
        } else {

            // TODO 작성 가능한 리뷰 목록 조회하는 기준 확인 필요

            // 모임이 종료되었지만 리뷰를 작성하지 않은 목록
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

            //작성한 리뷰 조회
            List<GatheringReviewDto> reviews = jpaQueryFactory
                    .select(Projections.constructor(GatheringReviewDto.class,
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

            result = fromGatheringReviews(gatheringResponses, list.size(), reviews, total);

            return result;
        }
    }

    @Override
    public ReviewListDto selectBookReviewList(String username) {

        User user = null;
        long myReviewCount = 0;
        List<BookResponse> unreviewsBookInfo = null;
        if(username != null) {
            user = userJpaRepository.findByUserNameOrThrow(username);
            // 작성한 독서 리뷰 갯수
            myReviewCount  = bookReviewJpaRepository.countByUserId(user.getId());

            // 모임이 종료되었지만 독서 리뷰를 작성하지 않은 책 정보 목록
            unreviewsBookInfo = findUnreviewedCompletedBook(user.getId());
        }

        // best 리뷰 목록
        List<BookReviewDto> bestReview = jpaQueryFactory
                .select(Projections.constructor(BookReviewDto.class,
                        bookReview.id
                        ,bookReview.title
                        ,bookReview.content
                        ,bookReview.likes
                        ,reviewCommnetCnt(bookReview.id)
                        ,JPAExpressions
                                .select(bookReview.count())
                                .from(bookReview)
                                .where(bookReview.user.id.eq(QUser.user.id))
                        ,QUser.user.userName
                        ,QUser.user.profile
                ))
                .from(bookReview)
                .leftJoin(QUser.user).on(bookReview.user.id.eq(QUser.user.id))
                .where(bookReview.user.id.eq(QUser.user.id).and(bookReview.status.eq(StatusType.Y)))
                .orderBy(bookReview.likes.desc())
                .limit(5)
                .fetch();

        ReviewListDto result = fromGatheringReviews(unreviewsBookInfo, bestReview, myReviewCount);
        return result;
    }

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!BookReviewTagType.ALL.getType().equals(tag.getType())) {
            builder.and(bookReview.tagCd.like("%" + tag + "%"));
            builder.and(bookReview.status.eq(StatusType.Y));
        }

        // Query 생성
        JPAQuery<BookReviewDto> query = jpaQueryFactory
                .select(Projections.constructor(BookReviewDto.class,
                        bookReview.id
                        ,bookReview.title
                        ,bookReview.apprCd
                        ,bookReview.book.image
                        ,bookReview.content
                        ,bookReview.likes
                        ,reviewCommnetCnt(bookReview.id)
                        ,bookReview.user.id
                        ,bookReview.user.profile
                        ,bookReview.user.userName
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime))
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.reviewComments, reviewComment)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        List<BookReviewDto> reviews = query.fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        if (hasNext) {
            reviews.remove(reviews.size() - 1);
        }

        return ReviewListDto.fromBookReviews(reviews, hasNext);
    }

    @Override
    public ReviewDto selectBookReviewDetail(long reviewId) {

        QBookReview subBookReview = new QBookReview("subBookReview");

        // 독서 리뷰 상세 정보
        BookReviewDto bookReviewDto = jpaQueryFactory
                .select(Projections.constructor(BookReviewDto.class,
                        bookReview.id
                        ,bookReview.book.id
                        ,bookReview.title
                        ,bookReview.apprCd
                        ,bookReview.tagCd
                        ,bookReview.content
                        ,bookReview.likes
                        ,bookReview.user.id
                        ,bookReview.user.profile
                        ,bookReview.user.userName
                        ,JPAExpressions
                                .select(subBookReview.count())
                                .from(subBookReview)
                                .where(subBookReview.user.id.eq(bookReview.user.id).and(subBookReview.status.eq(StatusType.Y)))
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime))
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.reviewComments, reviewComment)
                .where(bookReview.id.eq(reviewId)
                        .and(bookReview.status.eq(StatusType.Y)))
                .distinct()
                .fetchOne();

        if(bookReviewDto == null) {
            throw new BaseException(BaseResponseStatus.NON_EXISTED_REVIEW);
        }

        // 독서 리뷰에 대한 책 정보
        BookResponse bookResponse = jpaQueryFactory
                .select(Projections.constructor(BookResponse.class,
                        book.id
                        ,book.title
                        ,book.author
                        ,book.publisher
                        ,book.publishDate
                        ,book.star
                        ,book.image
                        ,JPAExpressions
                                .select(challenge.count())
                                .from(gathering)
                                .leftJoin(challenge).on(challenge.id.eq(gathering.challenge.id))
                                .where(gathering.book.id.eq(book.id)
                                        .and(challenge.challengeStatus.eq(ChallengeStatus.INACTIVE)))
                                .goe(1L)
                ))
                .from(book)
                .where(book.id.eq(bookReviewDto.getBookId()))
                .fetchOne();

        // 댓글 목록
        List<ReviewCommentDto> reviewCommentDto = jpaQueryFactory
                .select(Projections.constructor(ReviewCommentDto.class,
                        reviewComment.id
                        ,user.id
                        ,reviewComment.content
                        ,reviewComment.orders
                        ,user.profile
                        ,user.userName
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", reviewComment.createdTime)
                        ))
                .from(reviewComment)
                .leftJoin(user).on(user.id.eq(reviewComment.user.id))
                .where(reviewComment.review.id.eq(bookReviewDto.getId())
                        .and(reviewComment.status.eq(StatusType.Y)))
                .orderBy(reviewComment.orders.asc())
                .fetch();

        return new ReviewDto(bookReviewDto, bookResponse, reviewCommentDto);
    }

    /**
     * 리뷰 통합 검색 및 특정 리뷰 조회
     * @param type 검색 조건으로 리뷰 상세 페이지에서는 특정 책의 리뷰 목록을 조회
     * @param param 리뷰 상세 페이지에서는 첵 제목을 넘김
     * @return
     */
    @Override
    public ReviewListDto searchReviews(SearchType type, String param, Pageable pageable) {

        BooleanBuilder builder = queryBuilder.buildReviewSearch(type, param);
        // Query 생성
        JPAQuery<BookReviewDto> query = jpaQueryFactory
                .select(Projections.constructor(BookReviewDto.class,
                        bookReview.id
                        ,bookReview.title
                        ,bookReview.content
                        ,bookReview.likes
                        ,reviewCommnetCnt(bookReview.id)
                        ,bookReview.user.id
                        ,bookReview.user.profile
                        ,bookReview.user.userName
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime))
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.reviewComments, reviewComment)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<BookReviewDto> reviews = query.fetch();
        Long total = 0L;

        // 전체 데이터 개수 조회
        if(!reviews.isEmpty()) {
            total = jpaQueryFactory
                    .select(bookReview.count())
                    .from(bookReview)
                    .leftJoin(bookReview.user, user)
                    .leftJoin(bookReview.book, book)
                    .leftJoin(bookReview.reviewComments, reviewComment)
                    .where(builder)
                    .fetchOne();
        }
        return ReviewListDto.fromBookReviews(reviews, total);
    }

    @Transactional
    @Override
    public int DeleteBookReview(long reviewId,ReviewType type, String username) {
        User user = userJpaRepository.findByUserNameOrThrow(username);
        int result = 0;

        if(type.equals(ReviewType.BOOK)) {
            BookReview bookReview = bookReviewJpaRepository.findByIdOrThrow(reviewId);

            if(!bookReview.getUser().equals(user)) {
                throw new BaseException(BaseResponseStatus.REVIEW_OWNER_MISMATCH);
            }

            result = bookReviewJpaRepository.deleteReview(bookReview.getId(), StatusType.N);

        } else {
            GatheringReview gatheringReview = gatheringReviewJpaRepository.findByIdOrThrow(reviewId);

            if(!gatheringReview.getUser().equals(user)) {
                throw new BaseException(BaseResponseStatus.REVIEW_OWNER_MISMATCH);
            }

            result = gatheringReviewJpaRepository.deleteReview(gatheringReview.getId(), StatusType.N);
        }

        return result;
    }

    // 모임이 종료되었지만 모임 리뷰를 작성하지 않은 목록
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

    // 모임이 종료되었지만 독서 리뷰를 작성하지 않은 책 정보 목록
    private List<BookResponse> findUnreviewedCompletedBook(long userId) {

        return jpaQueryFactory
                .select(Projections.constructor(BookResponse.class,
                        gathering.book.id
                        ,gathering.book.title
                        ,gathering.book.author
                        ,gathering.book.publisher
                        ,gathering.book.publishDate
                        ,gathering.book.star
                        ,gathering.book.image
                        ,gathering.id))
                .from(gathering)
                .leftJoin(gathering.gatheringUsers, gatheringUser)
                .leftJoin(gathering.gatheringBookReviews, gatheringBookReview)
                .where(gatheringUserIdEq(userId),
                        gatheringStatusEq(GatheringStatus.COMPLETED),
                        gatheringBookReview.gathering.id.isNull())
                .fetch();
    }
    private BooleanExpression gatheringUserIdEq(long userId) {
        return isEmpty(userId) ? null : gatheringUser.user.id.eq(userId);
    }

    private BooleanExpression gatheringStatusEq(GatheringStatus gatheringStatus) {
        return isEmpty(gatheringStatus) ? null : gathering.gatheringStatus.eq(gatheringStatus);
    }

    // 리뷰 댓글 갯수 서브 쿼리
    public JPQLQuery<Long> reviewCommnetCnt(NumberPath<Long> reviewId) {
        return JPAExpressions
                .select(reviewComment.count())
                .from(reviewComment)
                .where(reviewComment.review.id.eq(reviewId));
    }
}
