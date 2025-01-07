package com.gathering.gatheringuser.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.gathering.gathering.model.entity.GatheringUserStatus.NOT_PARTICIPATING;

@Getter
@Entity
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

    public static GatheringUser createGatheringUser(UserDomain user, GatheringUserStatus gatheringUserStatus) {
//        GatheringUser gatheringUser = new GatheringUser();
//        gatheringUser.gatheringUserStatus = gatheringUserStatus;
//        gatheringUser.addUser(User.fromEntity(user));
//        return gatheringUser;
        return null;
    }

    public static GatheringUser fromEntity(GatheringUserDomain gatheringUser) {
        GatheringUser gatheringUserEntity = new GatheringUser();
        gatheringUserEntity.id = gatheringUser.getId();
        gatheringUserEntity.user = User.fromEntity(gatheringUser.getUser());
        Gathering gathering = Gathering.fromEntity(gatheringUser.getGathering());
        gatheringUserEntity.gathering = gathering;
        gathering.addGatheringUser(gatheringUserEntity);
        gatheringUserEntity.gatheringUserStatus = gatheringUser.getGatheringUserStatus();
        return gatheringUserEntity;
    }

    public void addUser(User user) {
        this.user = user;
        user.getGatheringUsers().add(this);
    }

    public void addGathering(Gathering gathering) {
        this.gathering = gathering;
    }

    public void endGatheringStatus() {
        this.gatheringUserStatus = NOT_PARTICIPATING;
    }

    public GatheringUserDomain toEntity() {
        return GatheringUserDomain.builder()
                .id(id)
                .user(user.toEntity())
                .gathering(gathering.toEntity())
                .gatheringUserStatus(gatheringUserStatus)
                .build();
    }
}
