package com.gathering.gathering.domain;

import com.gathering.book.domain.BookDomain;
import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.image.domain.ImageDomain;

import java.time.LocalDate;
import java.util.List;

public class GatheringSearchDomain {
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
}
