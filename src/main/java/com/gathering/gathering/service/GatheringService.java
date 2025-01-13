package com.gathering.gathering.service;

import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringUpdate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
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
