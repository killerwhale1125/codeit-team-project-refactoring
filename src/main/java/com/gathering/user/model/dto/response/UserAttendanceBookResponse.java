package com.gathering.user.model.dto.response;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.user.model.entitiy.UserAttendance;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserAttendanceBookResponse {

    private Date date;
    private List<BookResponse> bookResponses;
    private int totalBookCount;
    private long gatheringId;

    public static UserAttendanceBookResponse fromEntity(UserAttendance userAttendance) {
        List<BookResponse> bookResponses = userAttendance.getUserAttendanceBooks().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
        return UserAttendanceBookResponse.builder()
                .date(userAttendance.getCreateDate())
                .bookResponses(bookResponses)
                .totalBookCount(bookResponses.size())
                .build();
    }
}
