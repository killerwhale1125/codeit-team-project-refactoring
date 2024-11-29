package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringCreate;
import org.springframework.security.core.userdetails.UserDetails;

public interface GatheringService {
    void create(GatheringCreate gatheringCreate, UserDetails userDetails);
}
