package com.gathering.user.model.entitiy;

import com.gathering.challenge.model.entity.ChallengeUser;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private long id;
    private String userName;
    private String email;
    private String company;
    private String profile;

    @Embedded
    private Address address;

    @ColumnDefault("'USER'")
    private String roles = "USER"; // USER, ADMIN

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAttendance> userAttendances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }


    public static User createUser(SignUpRequestDto dto) {
        Address address = new Address(dto.getState(), dto.getCity(), dto.getTown());

        return User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .address(address)
                .build();
    }
}
