package com.gathering.gathering.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GatheringResultPage {

    private long id;
    private String name;
    private String thumbnail;
    private int readingTimeGoal;
    private int maxCapacity;
    private int currentCapacity;
    private String title;
    private String image;
}
