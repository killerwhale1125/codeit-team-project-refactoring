package com.gathering.gathering.model.domain;

import com.gathering.book.model.entity.Book;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.gathering.model.entity.GatheringBookReview;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.image.model.entity.Image;
import com.gathering.review.model.entitiy.GatheringReview;
import com.gathering.user.model.entitiy.UserAttendanceBook;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GatheringDomain {
    private Long id;
    private String name;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity = 0;
    private String owner;
    private long viewCount;
    private GatheringStatus gatheringStatus;

    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    private List<GatheringBookReview> gatheringBookReviews = new ArrayList<>();

    private List<GatheringReview> gatheringReviews = new ArrayList<>();

    private List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    private Challenge challenge;

    private Book book;

    private GatheringWeek gatheringWeek;

    private Image image;
}
