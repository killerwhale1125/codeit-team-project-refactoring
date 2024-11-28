package com.gathering.user.model.entitiy;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.user.model.dto.request.SignInRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
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
    private String roles; // USER, ADMIN

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAttendance> userAttendances;

    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }


}
