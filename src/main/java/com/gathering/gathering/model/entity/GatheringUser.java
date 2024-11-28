package com.gathering.gathering.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GatheringUserStatus gatheringUserStatus;

    public static GatheringUser create() {
        GatheringUser gatheringUser = new GatheringUser();
        gatheringUser.gatheringUserStatus = GatheringUserStatus.NOT_PARTICIPATING;
        return gatheringUser;
    }

    public void addUser(User user) {
        this.user = user;
    }
}
