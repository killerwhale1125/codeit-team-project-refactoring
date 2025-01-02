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
import com.gathering.review.model.entitiy.*;
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

import java.time.LocalDateTime;
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
import static com.gathering.review.model.entitiy.QReviewLikes.reviewLikes;
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
    private final ReviewLikesJpaRepository likesJpaRepository;

    @Transactional
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
                            bookReview.content,
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime),
                            bookReview.likes,
                            reviewCommnetCnt(bookReview.id),
                            likeUserCk(user)
                    ))
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


        // best 리뷰 목록
        JPAQuery<BookReviewDto> query = selectBestReview(5);

        if(username != null) {
            user = userJpaRepository.findByUserNameOrThrow(username);
            // 작성한 독서 리뷰 갯수
            myReviewCount  = bookReviewJpaRepository.countByUserId(user.getId());

            // 모임이 종료되었지만 독서 리뷰를 작성하지 않은 책 정보 목록
            unreviewsBookInfo = findUnreviewedCompletedBook(user.getId());

            // 출판날짜 포맷 형식 변경
            unreviewsBookInfo.forEach(book -> book.ChangeFormat(book.getPublisherDate()));

            query = query.select(Projections.constructor(BookReviewDto.class,
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
                    ,likeUserCk(user)
            ));
        }

        List<BookReviewDto> bestReview = query.fetch();

        ReviewListDto result = fromGatheringReviews(unreviewsBookInfo, bestReview, myReviewCount);
        return result;
    }

    @Override
    public ReviewListDto findReviews(BookReviewTagType tag, Pageable pageable, String username) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!BookReviewTagType.ALL.getType().equals(tag.getType())) {
            builder.and(bookReview.tagCd.like("%" + tag + "%"));
        }
        builder.and(bookReview.status.eq(StatusType.Y));
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
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime))
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.reviewComments, reviewComment)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);
            query = query.select(Projections.constructor(BookReviewDto.class,
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
                    ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime)
                    ,likeUserCk(user)
                    )
            );
        }


        List<BookReviewDto> reviews = query.fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        if (hasNext) {
            reviews.remove(reviews.size() - 1);
        }

        return ReviewListDto.fromBookReviews(reviews, hasNext);
    }

    @Override
    public ReviewDto selectBookReviewDetail(long reviewId, String username) {

        QBookReview subBookReview = new QBookReview("subBookReview");

        // 독서 리뷰 상세 정보
        
        // 비회원일 경우 좋아요 여부 미포함
        JPAQuery<BookReviewDto> query = jpaQueryFactory
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
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime)
                        ,bookReviewTypeCheck(bookReview.id)
                        )
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.reviewComments, reviewComment)
                .where(bookReview.id.eq(reviewId)
                        .and(bookReview.status.eq(StatusType.Y)))
                .distinct();
        
        // 로그인 회원일 경우 좋아요 여부 추가
        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);

            query = query.select(Projections.constructor(BookReviewDto.class,
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
                            ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime)
                            ,likeUserCk(user)
                            ,bookReviewTypeCheck(bookReview.id)
                    )

            );
        }

        BookReviewDto bookReviewDto = query.fetchOne();

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
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", reviewComment.createdTime)
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
    public ReviewListDto searchReviews(SearchType type, String param, Pageable pageable, String username) {

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
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 회원일 경우 좋아요 여부 포함
        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);
            query = query.select(Projections.constructor(BookReviewDto.class,
                            bookReview.id
                            ,bookReview.title
                            ,bookReview.content
                            ,bookReview.likes
                            ,reviewCommnetCnt(bookReview.id)
                            ,bookReview.user.id
                            ,bookReview.user.profile
                            ,bookReview.user.userName
                            ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime)
                            ,likeUserCk(user)
                    )
            );
        }


        List<BookReviewDto> reviews = query.fetch();
        Long total = 0L;

        // 전체 데이터 개수 조회
        if(!reviews.isEmpty()) {
            QBookReview subBookReview = new QBookReview("subBookReview");

            JPQLQuery<Long> subQuery = JPAExpressions
                    .select(bookReview.id)
                    .from(bookReview)
                    .leftJoin(bookReview.user, user)
                    .leftJoin(bookReview.book, book)
                    .leftJoin(bookReview.reviewComments, reviewComment)
                    .where(builder)
                    .distinct();

            total = jpaQueryFactory
                    .select(bookReview.count())
                    .from(bookReview)
                    .where(bookReview.id.in(subQuery))  // 서브쿼리를 where 절에서 사용
                    .fetchOne();
        }
        return ReviewListDto.fromBookReviews(reviews, total);
    }

    @Transactional
    @Override
    public int DeleteReview(long reviewId,ReviewType type, String username) {
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
    @Transactional
    @Override
    public void UpdateReview(CreateReviewDto createReviewDto, long reviewId, ReviewType type, String username) {

        User user = userJpaRepository.findByUserNameOrThrow(username);
        try {
            if(type.equals(ReviewType.BOOK)) {
                BookReview bookReview = bookReviewJpaRepository.findByIdOrThrow(reviewId);

                if(!bookReview.getUser().equals(user)) {
                    throw new BaseException(BaseResponseStatus.REVIEW_OWNER_MISMATCH);
                }


                Optional<Book> book = bookJpaRepository.findById(bookReview.getBook().getId());
                bookReview.updateReview(createReviewDto, book.get());

            } else {
                GatheringReview gatheringReview = gatheringReviewJpaRepository.findByIdOrThrow(reviewId);

                if(!gatheringReview.getUser().equals(user)) {
                    throw new BaseException(BaseResponseStatus.REVIEW_OWNER_MISMATCH);
                }

                gatheringReview.updateReview(createReviewDto);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.REVIEW_UPDATE_FAILED);
        }
    }

    @Override
    public void UpdateReviewLike(ReviewLikeDto reviewLikeDto, String username) {
        User user = userJpaRepository.findByUserNameOrThrow(username);

        Optional<ReviewLikes> like = likesJpaRepository.findByReviewIdAndUserId(reviewLikeDto.getReviewId(), user.getId());
        BookReview bookReview = bookReviewJpaRepository.findByIdOrThrow(reviewLikeDto.getReviewId());

        /**
         * 이미 좋아요가 있을 경우 좋아요 취소
         * 좋아요가 없을경우 추가
         */
        boolean bol = like.isPresent();

        if(bol) {
            likesJpaRepository.delete(like.get());
            bookReviewJpaRepository.decrementLikes(like.get().getReview().getId());
        } else {
            ReviewLikes likes = ReviewLikes.createEntity(bookReview, user);
            likesJpaRepository.save(likes);
            bookReviewJpaRepository.incrementLikes(likes.getReview().getId());
        }

    }

    @Transactional
    @Override
    public int DeleteComment(long commentId, String username) {
        User user = userJpaRepository.findByUserNameOrThrow(username);

        ReviewComment reviewComment = reviewCommentJpaRepository.findByIdOrThrow(commentId);

        if(reviewComment.getUser().getId() != user.getId()) {
            throw new BaseException(BaseResponseStatus.COMMENT_OWNER_MISMATCH);
        }

        return reviewCommentJpaRepository.deleteComment(reviewComment.getId(), StatusType.N);

    }

    @Override
    public int UpdateComment(long commentId, CreateReviewCommentDto updateReviewCommentDto, String username) {

        User user = userJpaRepository.findByUserNameOrThrow(username);

        ReviewComment reviewComment = reviewCommentJpaRepository.findByIdOrThrow(commentId);

        if(reviewComment.getUser().getId() != user.getId()) {
            throw new BaseException(BaseResponseStatus.COMMENT_OWNER_MISMATCH);
        }

        return reviewCommentJpaRepository.UpdateComment(reviewComment.getId(), updateReviewCommentDto.getContent(), LocalDateTime.now());
    }

    @Override
    public List<BookResponse> searchUserGatheringBooks(String username) {
        User user = userJpaRepository.findByUserNameOrThrow(username);

        return findUnreviewedCompletedBook(user.getId());
    }

    @Override
    public List<BookResponse> getRecommendedKeywords() {

        // best 리뷰 목록
        List<BookReviewDto> bookReviews = selectBestReview(99).fetch();

        // 리뷰 아이디 목록
        List<Long> ids = bookReviews.stream().map(BookReviewDto::getId).toList();


        return jpaQueryFactory
                .select(Projections.constructor(BookResponse.class,
                        book.id
                        ,book.title
                ))
                .from(bookReview)
                .leftJoin(book).on(bookReview.book.eq(book))
                .where(bookReview.status.eq(StatusType.Y)
                .and(bookReview.id.in(ids)))
                .limit(7)
                .distinct()
                .fetch();
    }

    // best 리뷰 목록 조회
    private JPAQuery<BookReviewDto> selectBestReview(int cnt) {

        return jpaQueryFactory
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
                .limit(cnt);
    };

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
                .where(reviewComment.review.id.eq(reviewId)
                        .and(reviewComment.status.eq(StatusType.Y)));
    }

    // 사용자 좋아요 여부 서브 쿼리
    public BooleanExpression likeUserCk(User user) {
        return JPAExpressions
                .select(reviewLikes.count())
                .from(reviewLikes)
                .where(reviewLikes.user.id.eq(user.getId()).and(reviewLikes.review.id.eq(bookReview.id)))
                .gt(0L);
    }


    // 리뷰가 모임에서 읽은 책일경우 모임 아이디 반환
    public JPQLQuery<Long> bookReviewTypeCheck(NumberPath<Long> reviewId) {
        return JPAExpressions
                .select(gatheringBookReview.id)
                .from(gatheringBookReview)
                .where(gatheringBookReview.review.id.eq(reviewId)
                        .and(gatheringBookReview.status.eq(StatusType.Y)));
    }
}
