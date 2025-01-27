package com.gathering.user.infrastructure.entitiy;

import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.challenge_user.infrastructure.entity.ChallengeUser;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering_user.infrastructure.entity.GatheringUser;
import com.gathering.image.infrastructure.entity.Image;
import com.gathering.user.domain.UserDomain;
import com.gathering.user_attendance.infrastructure.entity.UserAttendance;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.*;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @Comment("사용자 pk")
    private Long id;

    @Column(unique = true)
    @Comment("사용자 아이디")
    private String userName;
    
    @Comment("비밀번호")
    private String password;

    @Column(unique = true)
    @Comment("이메일")
    private String email;

    @Comment("프로필")
    private String profile;

    @ColumnDefault("'USER'")
    @Comment("권한")
    private String roles; // USER, ADMIN

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "user")
    private List<UserAttendance> userAttendances;

    @OneToMany(mappedBy = "user")
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookReview> bookReviews = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_wishes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "gathering_id")
    private Set<Long> wishGatheringIds = new HashSet<>();

    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

    public static User fromEntity(UserDomain user) {
        User userEntity = new User();
        userEntity.id = user.getId();
        userEntity.userName = user.getUserName();
        userEntity.password = user.getPassword();
        userEntity.email = user.getEmail();
        userEntity.profile = user.getProfile();
        userEntity.roles = user.getRoles();
        userEntity.image = nullableEntity(Image::fromEntity, user.getImage());

        return userEntity;
    }

    public UserDomain toEntity() {
        UserDomain.UserDomainBuilder builder = UserDomain.builder()
                .id(id)
                .userName(userName)
                .password(password)
                .email(email)
                .profile(profile)
                .roles(roles)
                .createdTime(createdTime)
                .modifiedTime(modifiedTime);

        if (wishGatheringIds != null) {
            builder.wishGatheringIds(wishGatheringIds);
        }

        if (image != null && getPersistenceUtil().isLoaded(image)) {
            builder.image(image.toEntity());
        }
        return builder.build();
    }
}
