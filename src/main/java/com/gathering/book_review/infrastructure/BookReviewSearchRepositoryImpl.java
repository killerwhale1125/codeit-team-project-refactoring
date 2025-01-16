package com.gathering.book_review.infrastructure;

import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.review.domain.StatusType;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.infrastructure.entitiy.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.book_review.infrastructure.entity.QBookReview.bookReview;
import static com.gathering.book_review_comment.infrastructure.entity.QReviewComment.reviewComment;
import static com.gathering.review_like.infrastructure.entity.QReviewLikes.reviewLikes;

@Repository
@RequiredArgsConstructor
public class BookReviewSearchRepositoryImpl implements BookReviewSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

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
                        reviewCommnetCnt(bookReview.id),
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

    public BooleanExpression likeUserCk(User user) {
        return JPAExpressions
                .select(reviewLikes.count())
                .from(reviewLikes)
                .where(reviewLikes.user.id.eq(user.getId()).and(reviewLikes.review.id.eq(bookReview.id)))
                .gt(0L);
    }

    // 리뷰 댓글 갯수 서브 쿼리
    public JPQLQuery<Long> reviewCommnetCnt(NumberPath<Long> reviewId) {
        return JPAExpressions
                .select(reviewComment.count())
                .from(reviewComment)
                .where(reviewComment.review.id.eq(reviewId)
                        .and(reviewComment.status.eq(StatusType.Y)));
    }
}
