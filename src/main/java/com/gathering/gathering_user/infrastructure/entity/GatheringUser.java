package com.gathering.gathering_user.infrastructure.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.gathering.gathering_user.domain.GatheringUserDomain;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gathering.gathering_user.domain.GatheringUserStatus.NOT_PARTICIPATING;
import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

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
                .gatheringUserStatus(gatheringUserStatus);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
