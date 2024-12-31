package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringUpdate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.user.model.dto.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface GatheringService {
    void create(GatheringCreate gatheringCreate, List<MultipartFile> files, String username);

    void join(Long gatheringId, String username);

    void delete(Long gatheringId, String username);

    List<UserResponseDto> findGatheringWithUsersByIdAndStatus(Long gatheringId, GatheringUserStatus gatheringStatus);

    void leave(Long gatheringId, String username, GatheringUserStatus gatheringUserStatus);

    void wish(Long gatheringId, String username);

    void update(GatheringUpdate gatheringUpdate, String username);

    LocalDate calculateEndDate(LocalDate startDate, GatheringWeek gatheringWeek);

    MyPageGatheringsCountResponse getMyPageGatheringsCount(String username);

    void readBook(String username, long gatheringId);

//    void incrementViewCountAsync(Long gatheringId);
}
