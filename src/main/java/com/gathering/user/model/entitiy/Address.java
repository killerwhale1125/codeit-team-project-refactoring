package com.gathering.user.model.entitiy;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String state;
    private String city;
    private String town;

    protected Address() {}

    public Address(String state, String city, String town) {
        this.state = state;
        this.city = city;
        this.town = town;
    }
}
