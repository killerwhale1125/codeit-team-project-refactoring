package com.gathering.user.model.entitiy;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
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
    @JoinColumn(name = "USER_ID")
    private User user;

    private Date createDate;

}
