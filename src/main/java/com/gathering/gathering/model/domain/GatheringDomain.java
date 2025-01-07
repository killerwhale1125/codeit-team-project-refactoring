package com.gathering.gathering.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.domain.BookDomain;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.user.model.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringDomain {
    private Long id;
    private String name;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity;
    private String owner;
    private long viewCount;
    private GatheringStatus gatheringStatus;
    private List<GatheringUserDomain> gatheringUsers;
    private ChallengeDomain challenge;
    private BookDomain book;
    private GatheringWeek gatheringWeek;
    private ImageDomain image;

    public static GatheringDomain create(GatheringCreate gatheringCreate, ChallengeDomain challenge, BookDomain book, List<ImageDomain> images, UserDomain user) {
        GatheringDomain gathering = GatheringDomain.builder()
                .name(gatheringCreate.getName())
                .content(gatheringCreate.getContent())
                .startDate(gatheringCreate.getStartDate())
                .endDate(gatheringCreate.getEndDate())
                .minCapacity(gatheringCreate.getMinCapacity())
                .maxCapacity(gatheringCreate.getMaxCapacity())
                .currentCapacity(1)
                .owner(user.getUserName())
                .viewCount(0)
                .gatheringStatus(gatheringCreate.getGatheringStatus())
                .gatheringUsers(new ArrayList<>())
                .challenge(challenge)
                .book(book)
                .gatheringWeek(gatheringCreate.getGatheringWeek())
                .image(images.get(0))
                .build();
        return gathering;
    }
}
