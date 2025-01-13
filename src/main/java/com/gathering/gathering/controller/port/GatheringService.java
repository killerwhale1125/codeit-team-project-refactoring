package com.gathering.gathering.controller.port;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringCreate;
import com.gathering.gathering.controller.response.MyPageGatheringsCountResponse;
import com.gathering.gathering.infrastructure.entity.GatheringUserStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user_attendance_book.model.domain.UserAttendanceBookDomain;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface GatheringService {
    GatheringDomain create(GatheringCreate gatheringCreate, List<MultipartFile> files, String username);

    void join(Long gatheringId, String username);

    void delete(Long gatheringId, String username);

    List<UserResponseDto> findByGatheringIdAndStatusWithUsers(Long gatheringId, GatheringUserStatus gatheringUserStatus);

    void leave(Long gatheringId, String username);

    void wish(Long gatheringId, String username);

    LocalDate calculateEndDate(LocalDate startDate, GatheringWeek gatheringWeek);

    MyPageGatheringsCountResponse getMyPageGatheringsCount(String username);

    void end(Long challengeId);

    UserAttendanceBookDomain readBookCompleted(String username, Long gatheringId);

//    void incrementViewCountAsync(Long gatheringId);
}
