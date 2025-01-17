package com.gathering.book_review.infrastructure;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book.domain.BookDomain;
import com.gathering.book.infrastructure.entity.Book;
import com.gathering.book_review.controller.response.BookReviewDetailsResponse;
import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.domain.BookReviewTagType;
import com.gathering.book_review.domain.StatusType;
import com.gathering.book_review.infrastructure.entity.QBookReview;
import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.challenge.infrastructure.entity.ChallengeStatus;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.SearchType;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.infrastructure.UserJpaRepository;
import com.gathering.user.infrastructure.entitiy.QUser;
import com.gathering.user.infrastructure.entitiy.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.book.infrastructure.entity.QBook.book;
import static com.gathering.book_review.infrastructure.entity.QBookReview.bookReview;
import static com.gathering.book_review_comment.infrastructure.entity.QBookReviewComment.bookReviewComment;
import static com.gathering.book_review_like.infrastructure.entity.QBookReviewLike.bookReviewLike;
import static com.gathering.challenge.infrastructure.entity.QChallenge.challenge;
import static com.gathering.gathering.infrastructure.entity.QGathering.gathering;
import static com.gathering.gathering_user.infrastructure.entity.QGatheringUser.gatheringUser;
import static com.gathering.user.infrastructure.entitiy.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class BookReviewSearchRepositoryImpl implements BookReviewSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepository userJpaRepository;
    private final BookReviewJpaRepository bookReviewJpaRepository;

    @Override
    public BookReviewsResponse getMyReviews(UserDomain user, int page, int size) {
        User userEntity = User.fromEntity(user);
        Pageable pageable = PageRequest.of(page, size);

        List<BookReviewResponse> reviews = jpaQueryFactory
                .select(Projections.constructor(BookReviewResponse.class,
                        bookReview.id,
                        bookReview.title,
                        bookReview.content,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime),
                        bookReview.likes,
                        reviewCommentCnt(bookReview.id),
                        likeUserCk(userEntity),
                        bookReview.book.title
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

        return BookReviewsResponse.fromEntity(reviews, total);
    }

    @Override
    public BookReviewsResponse getBookReviews(String username) {
        User user = null;
        long myReviewCount = 0;
        List<BookResponse> unreviewsBookInfo = null;


        // best 리뷰 목록
        JPAQuery<BookReviewResponse> query = selectBestReview(5);

        if(username != null) {
            user = userJpaRepository.findByUserNameOrThrow(username);
            // 작성한 독서 리뷰 갯수
            myReviewCount  = bookReviewJpaRepository.countByUserId(user.getId());

            // 모임이 종료되었지만 독서 리뷰를 작성하지 않은 책 정보 목록
            unreviewsBookInfo = findUnreviewedCompletedBook(user.getId());

            // 출판날짜 포맷 형식 변경
            unreviewsBookInfo.forEach(book -> book.ChangeFormat(book.getPublisherDate()));

            query = query.select(Projections.constructor(BookReviewResponse.class,
                    bookReview.id
                    ,bookReview.title
                    ,bookReview.content
                    ,bookReview.likes
                    ,reviewCommentCnt(bookReview.id)
                    ,JPAExpressions
                            .select(bookReview.count())
                            .from(bookReview)
                            .where(bookReview.user.id.eq(QUser.user.id))
                    , QUser.user.userName
                    ,QUser.user.profile
                    ,likeUserCk(user)
            ));
        }

        List<BookReviewResponse> bestReview = query.fetch();

        return BookReviewsResponse.fromEntity(unreviewsBookInfo, bestReview, myReviewCount);
    }

    @Override
    public BookReviewsResponse findReviewsByTag(String username, BookReviewTagType tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        BooleanBuilder builder = new BooleanBuilder();
        if (!BookReviewTagType.ALL.getType().equals(tag.getType())) {
            builder.and(bookReview.tagCd.like("%" + tag + "%"));
        }

        builder.and(bookReview.status.eq(StatusType.Y));

        // Query 생성
        JPAQuery<BookReviewResponse> query = jpaQueryFactory
                .select(Projections.constructor(BookReviewResponse.class,
                        bookReview.id
                        ,bookReview.title
                        ,bookReview.apprCd
                        ,bookReview.book.image
                        ,bookReview.content
                        ,bookReview.likes
                        ,reviewCommentCnt(bookReview.id)
                        ,bookReview.user.id
                        ,bookReview.user.profile
                        ,bookReview.user.userName
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime))
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.bookReviewComments, bookReviewComment)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);
            query = query.select(Projections.constructor(BookReviewResponse.class,
                            bookReview.id
                            ,bookReview.title
                            ,bookReview.apprCd
                            ,bookReview.book.image
                            ,bookReview.content
                            ,bookReview.likes
                            ,reviewCommentCnt(bookReview.id)
                            ,bookReview.user.id
                            ,bookReview.user.profile
                            ,bookReview.user.userName
                            ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReview.createdTime)
                            ,likeUserCk(user)
                    )
            );
        }

        List<BookReviewResponse> reviews = query.fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        if (hasNext) {
            reviews.remove(reviews.size() - 1);
        }
        return BookReviewsResponse.fromBookReviews(reviews, hasNext);
    }

    @Override
    public BookReviewDetailsResponse getDetailBy(long reviewId, String username) {
        QBookReview subBookReview = new QBookReview("subBookReview");

        // 독서 리뷰 상세 정보

        // 비회원일 경우 좋아요 여부 미포함
        JPAQuery<BookReviewResponse> query = jpaQueryFactory
                .select(Projections.constructor(BookReviewResponse.class,
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
                        )
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.bookReviewComments, bookReviewComment)
                .where(bookReview.id.eq(reviewId)
                        .and(bookReview.status.eq(StatusType.Y)))
                .distinct();

        // 로그인 회원일 경우 좋아요 여부 추가
        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);

            query = query.select(Projections.constructor(BookReviewResponse.class,
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
                    )

            );
        }

        BookReviewResponse bookReviewDto = query.fetchOne();

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
        List<BookReviewCommentResponse> reviewCommentDto = jpaQueryFactory
                .select(Projections.constructor(BookReviewCommentResponse.class,
                        bookReviewComment.id
                        ,user.id
                        ,bookReviewComment.content
                        ,bookReviewComment.orders
                        ,user.profile
                        ,user.userName
                        ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", bookReviewComment.createdTime)
                ))
                .from(bookReviewComment)
                .leftJoin(user).on(user.id.eq(bookReviewComment.user.id))
                .where(bookReviewComment.bookReview.id.eq(bookReviewDto.getId())
                        .and(bookReviewComment.status.eq(StatusType.Y)))
                .orderBy(bookReviewComment.orders.asc())
                .fetch();

        return BookReviewDetailsResponse.fromEntity(bookReviewDto, bookResponse, reviewCommentDto);
    }

    @Override
    public BookReviewsResponse searchByWord(SearchType type, String searchWord, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        BooleanBuilder builder = new BooleanBuilder();

        if(type.equals(SearchType.BOOK_NAME)) {
            if(searchWord != null && !searchWord.isEmpty()) {
                builder.and(bookReview.book.title.like("%"+ searchWord + "%"));
            }
        } else if(type.equals(SearchType.TITLE)) {
            if(searchWord != null && !searchWord.isEmpty()) {
                builder.and(bookReview.title.like("%"+ searchWord + "%"));
            }
        } else {
            if(searchWord != null && !searchWord.isEmpty()) {
                builder.and(bookReview.content.like("%"+ searchWord + "%"));
            }
        }

        builder.and(bookReview.status.eq(StatusType.Y));

        JPAQuery<BookReviewResponse> query = jpaQueryFactory
                .select(Projections.constructor(BookReviewResponse.class,
                                bookReview.id
                                ,bookReview.title
                                ,bookReview.content
                                ,bookReview.likes
                                ,reviewCommentCnt(bookReview.id)
                                ,bookReview.user.id
                                ,bookReview.user.profile
                                ,bookReview.user.userName
                                ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime)
                                ,book.title
                        )
                )
                .from(bookReview)
                .leftJoin(bookReview.user, user)
                .leftJoin(bookReview.book, book)
                .leftJoin(bookReview.bookReviewComments, bookReviewComment)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(username != null) {
            User user = userJpaRepository.findByUserNameOrThrow(username);
            query = query.select(Projections.constructor(BookReviewResponse.class,
                            bookReview.id
                            ,bookReview.title
                            ,bookReview.content
                            ,bookReview.likes
                            ,reviewCommentCnt(bookReview.id)
                            ,bookReview.user.id
                            ,bookReview.user.profile
                            ,bookReview.user.userName
                            ,Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", bookReview.createdTime)
                            ,likeUserCk(user)
                            ,book.title
                    )
            );
        }

        List<BookReviewResponse> reviews = query.fetch();
        Long total = 0L;

        // 전체 데이터 개수 조회
        if(!reviews.isEmpty()) {
            QBookReview subBookReview = new QBookReview("subBookReview");

            JPQLQuery<Long> subQuery = JPAExpressions
                    .select(bookReview.id)
                    .from(bookReview)
                    .leftJoin(bookReview.user, user)
                    .leftJoin(bookReview.book, book)
                    .leftJoin(bookReview.bookReviewComments, bookReviewComment)
                    .where(builder)
                    .distinct();

            total = jpaQueryFactory
                    .select(bookReview.count())
                    .from(bookReview)
                    .where(bookReview.id.in(subQuery))  // 서브쿼리를 where 절에서 사용
                    .fetchOne();
        }

        return BookReviewsResponse.fromEntity(reviews, total);
    }

    @Override
    public BookReviewDomain findByIdWithUser(Long reviewId) {
        return bookReviewJpaRepository.findByIdWithUser(reviewId).toEntity();
    }

    @Override
    public List<BookDomain> getWroteReviewBooksCount(List<Long> bookIds, Long userId) {
        return bookReviewJpaRepository.getWroteReviewBooksCount(bookIds, userId).stream()
                .map(Book::toEntity)
                .collect(Collectors.toList());
    }

    private BooleanExpression likeUserCk(User user) {
        return JPAExpressions
                .select(bookReviewLike.count())
                .from(bookReviewLike)
                .where(bookReviewLike.user.id.eq(user.getId()).and(bookReviewLike.bookReview.id.eq(bookReview.id)))
                .gt(0L);
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
                .where(gatheringUserIdEq(userId),
                        gatheringStatusEq(GatheringStatus.COMPLETED))
                .fetch();
    }

    private JPAQuery<BookReviewResponse> selectBestReview(int cnt) {

        return jpaQueryFactory
                .select(Projections.constructor(BookReviewResponse.class,
                        bookReview.id
                        ,bookReview.title
                        ,bookReview.content
                        ,bookReview.likes
                        ,reviewCommentCnt(bookReview.id)
                        ,JPAExpressions
                                .select(bookReview.count())
                                .from(bookReview)
                                .where(bookReview.user.id.eq(user.id))
                        , user.userName
                        , user.profile
                ))
                .from(bookReview)
                .leftJoin(user).on(bookReview.user.id.eq(user.id))
                .where(bookReview.user.id.eq(user.id).and(bookReview.status.eq(StatusType.Y)))
                .orderBy(bookReview.likes.desc())
                .limit(cnt);
    };

    // 리뷰 댓글 갯수 서브 쿼리
    private JPQLQuery<Long> reviewCommentCnt(NumberPath<Long> reviewId) {
        return JPAExpressions
                .select(bookReviewComment.count())
                .from(bookReviewComment)
                .where(bookReviewComment.bookReview.id.eq(reviewId)
                        .and(bookReviewComment.status.eq(StatusType.Y)));
    }

    private BooleanExpression gatheringUserIdEq(long userId) {
        return isEmpty(userId) ? null : gatheringUser.user.id.eq(userId);
    }

    private BooleanExpression gatheringStatusEq(GatheringStatus gatheringStatus) {
        return isEmpty(gatheringStatus) ? null : gathering.gatheringStatus.eq(gatheringStatus);
    }
}
