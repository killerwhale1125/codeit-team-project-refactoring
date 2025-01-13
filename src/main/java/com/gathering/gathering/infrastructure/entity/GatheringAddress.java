package com.gathering.gathering.infrastructure.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class GatheringAddress {
    private String state;
    private String city;
    private String town;

    public GatheringAddress(){}

    public GatheringAddress(String state, String city, String town) {
        this.state = state;
        this.city = city;
        this.town = town;
    }
}
