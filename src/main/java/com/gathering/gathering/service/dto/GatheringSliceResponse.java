package com.gathering.gathering.service.dto;

import com.gathering.gathering.domain.GatheringDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GatheringSliceResponse {

    private List<GatheringDomain> gatherings;
    private boolean hasNext;
}
