package com.gathering.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.challenge_user.infrastructure.entity.ChallengeUser;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering_user.infrastructure.entity.GatheringUser;
import com.gathering.image.domain.ImageDomain;
import com.gathering.book_review_like.infrastructure.entity.BookReviewLike;
import com.gathering.user.util.PasswordEncoderHolder;
import com.gathering.user_attendance.infrastructure.entity.UserAttendance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDomain {
    private Long id;

    private String userName;

    private String password;

    private String email;

    private String profile;

    private String roles; // USER, ADMIN

    private ImageDomain image;

    private List<UserAttendance> userAttendances;

    private List<BookReviewLike> bookReviewLikes;

    private List<GatheringUser> gatheringUsers;

    private List<ChallengeUser> challengeUsers;

    private List<BookReview> reviews;

    private Set<Long> wishGatheringIds;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    public static void wishGathering(UserDomain user, Long gatheringId) {
        Set<Long> gatheringIds = user.getWishGatheringIds();
        if(gatheringIds.contains(gatheringId)) {
            gatheringIds.remove(gatheringId);
        } else {
            gatheringIds.add(gatheringId);
        }
    }

    public static UserDomain signUp(UserSignUp userSignUp, ImageDomain image, PasswordEncoderHolder passwordEncoder) {
        return UserDomain.builder()
                .roles("USER")
                .userName(userSignUp.getUserName())
                .password(passwordEncoder.encode(userSignUp.getPassword()))
                .email(userSignUp.getEmail())
                .image(image)
                .profile(image.getUrl())
                .build();
    }

    public UserDomain update(UserUpdate userUpdate, ImageDomain image, PasswordEncoderHolder passwordEncoder) {
        String newPwd = userUpdate.getPassword() != null ? passwordEncoder.encode(userUpdate.getPassword()) : password;
        return UserDomain.builder()
                .id(id)
                .userName(userUpdate.getUserName())
                .password(newPwd)
                .email(userUpdate.getEmail())
                .profile(image.getUrl())
                .image(image)
                .roles(roles)
                .createdTime(createdTime)
                .modifiedTime(modifiedTime)
                .build();
    }

    public UserDomain login(UserLogin userLogin, PasswordEncoderHolder passwordEncoder) {
        if(!passwordEncoder.verifyPassword(userLogin.password(), password)) {
            throw new BaseException(BaseResponseStatus.SIGN_IN_FAIL);
        }
        return this;
    }
}
