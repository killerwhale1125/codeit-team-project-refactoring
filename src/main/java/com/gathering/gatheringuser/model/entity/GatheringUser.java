package com.gathering.gatheringuser.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.Getter;

import static com.gathering.gathering.model.entity.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

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

    public void endGatheringStatus() {
        this.gatheringUserStatus = NOT_PARTICIPATING;
    }

    public static GatheringUser fromEntity(GatheringUserDomain gatheringUser) {
        GatheringUser gatheringUserEntity = new GatheringUser();
        gatheringUserEntity.id = gatheringUser.getId();

        User user = nullableEntity(User::fromEntity, gatheringUser.getUser());
        if(user != null) {
            gatheringUserEntity.user = user;
            user.getGatheringUsers().add(gatheringUserEntity);
        }

        Gathering gathering = nullableEntity(Gathering::fromEntity, gatheringUser.getGathering());
        if(gathering != null) {
            gatheringUserEntity.gathering = gathering;
            gathering.getGatheringUsers().add(gatheringUserEntity);
        }

        gatheringUserEntity.gatheringUserStatus = gatheringUser.getGatheringUserStatus();
        return gatheringUserEntity;
    }

    public GatheringUserDomain toEntity() {
        GatheringUserDomain.GatheringUserDomainBuilder builder = GatheringUserDomain.builder()
                .id(id)
                .user(user.toEntity())
                .gatheringUserStatus(gatheringUserStatus);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
