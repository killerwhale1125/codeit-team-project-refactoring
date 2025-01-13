package com.gathering.user_attendance.model.entity;

import com.gathering.user.model.entitiy.User;
import com.gathering.user_attendance.model.domain.UserAttendanceDomain;
import com.gathering.user_attendance_book.model.entity.UserAttendanceBook;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

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

    @OneToMany(mappedBy = "userAttendance")
    private List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    public static UserAttendance fromEntity(UserAttendanceDomain userAttendance) {
        UserAttendance userAttendanceEntity = new UserAttendance();
        userAttendanceEntity.id = userAttendance.getId();
        userAttendanceEntity.createDate = userAttendance.getCreateDate();
        userAttendanceEntity.user = nullableEntity(User::fromEntity, userAttendance.getUser());
        return userAttendanceEntity;

    }

    public UserAttendanceDomain toEntity() {
        UserAttendanceDomain.UserAttendanceDomainBuilder builder = UserAttendanceDomain.builder()
                .id(id)
                .createDate(createDate);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        if (userAttendanceBooks != null && getPersistenceUtil().isLoaded(userAttendanceBooks)) {
            builder.userAttendanceBooks(userAttendanceBooks.stream().map(UserAttendanceBook::toEntity).collect(Collectors.toList()));
        }

        return builder.build();
    }
}
