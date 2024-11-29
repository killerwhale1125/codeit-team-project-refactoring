package com.gathering.gathering.model.dto;

import com.gathering.gathering.model.entity.GatheringAddress;
import com.gathering.gathering.model.entity.GatheringType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GatheringCreate {
    private String name;    // 제목
    private GatheringType gatheringType;
    private String state;
    private String city;
    private String town;
    private String content;
    private LocalDateTime endDate;
    private LocalDateTime gatheringDate;
    private int minCapacity;
    private int maxCapacity;

    public GatheringAddress toAddress() {
        return new GatheringAddress(state, city, town);
    }
}
