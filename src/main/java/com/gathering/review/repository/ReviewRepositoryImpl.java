package com.gathering.review.repository;

import com.gathering.book.model.entity.Book;
import com.gathering.book.repository.BookJpaRepository;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringBookReview;
import com.gathering.gathering.repository.GatheringJpaRepository;
import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.dto.ReviewDto;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import com.gathering.review.model.entitiy.ReviewComment;
import com.gathering.user.model.constant.REVIEWTYPE;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gathering.review.model.entitiy.QReviewComment.reviewComment;

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
    @Override
    public ReviewDto createReview(CreateReviewDto createReviewDto, String username, String type) {

        User user = userJpaRepository.findByUserNameOrThrow(username);
        Gathering gathering = null;
        if(createReviewDto.getGatheringId() != 0) {
            gathering = gatheringJpaRepository.findById(createReviewDto.getGatheringId())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_GATHERING));
        }

        // 모임 리뷰
        if(type.equalsIgnoreCase(REVIEWTYPE.GATHERING.getValue())) {
            GatheringReview review = GatheringReview.createEntity(gathering, user, createReviewDto);

            review = gatheringReviewJpaRepository.save(review);

            return ReviewDto.formEntity(review);
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
            return ReviewDto.formEntity(review);
        }

    }

    @Override
    public ReviewDto createReviewComment(CreateReviewCommentDto createReviewCommentDto, String username) {

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

        reviewCommentJpaRepository.save(reviewComment);

        return null;
    }
}
