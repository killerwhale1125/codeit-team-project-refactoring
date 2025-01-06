package com.gathering.user.model.entitiy;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "USER_ATTENDANCE",
        uniqueConstraints={
                @UniqueConstraint(
                        name="USER_ATTENDANCE_UK",
                        columnNames={"USER_ID", "CREATE_DATE"}
                )
        })
public class UserAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATNDC_ID")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Date createDate;

    @OneToMany(mappedBy = "userAttendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

}
