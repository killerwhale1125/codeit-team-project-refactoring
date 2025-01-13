package com.gathering.user.model.entitiy;

import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gatheringuser.model.entity.GatheringUser;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.ReviewLikes;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user_attendance.model.entity.UserAttendance;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.*;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@Table(name = "users")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @Comment("사용자 pk")
    private long id;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAttendance> userAttendances;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ReviewLikes> reviewLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookReview> reviews = new ArrayList<>();

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

    public static User createUser(SignUpRequestDto dto) {
        return User.builder()
                .roles("USER")
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();
    }

    public static User fromDto(UserDto userDto) {
        return User.builder()
                .roles("USER")
                .userName(userDto.getUserName())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .build();
    }

    public static User fromEntity(UserDomain user) {
        User userEntity = new User();
        userEntity.id = user.getId();
        userEntity.userName = user.getUserName();
        userEntity.password = user.getPassword();
        userEntity.email = user.getEmail();
        userEntity.profile = user.getProfile();
        userEntity.roles = user.getRoles();
        return userEntity;
    }

    public UserDomain toEntity() {
        UserDomain.UserDomainBuilder builder = UserDomain.builder()
                .id(id)
                .userName(userName)
                .password(password)
                .email(email)
                .profile(profile)
                .roles(roles);

        if (wishGatheringIds != null) {
            builder.wishGatheringIds(wishGatheringIds);
        }
        return builder.build();
    }
}
