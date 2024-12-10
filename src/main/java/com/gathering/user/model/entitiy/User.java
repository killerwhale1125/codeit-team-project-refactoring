package com.gathering.user.model.entitiy;

import com.gathering.challenge.model.entity.ChallengeUser;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.review.model.entitiy.Review;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.*;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_wishes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "gathering_id")
    private Set<Long> wishGatheringIds = new HashSet<>();

    public static void addWish(User user, Long gatheringId) {
        Set<Long> gatheringIds = user.getWishGatheringIds();
        if(gatheringIds.contains(gatheringId)) {
            gatheringIds.remove(gatheringId);
        } else {
            gatheringIds.add(gatheringId);
        }
    }

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
}
