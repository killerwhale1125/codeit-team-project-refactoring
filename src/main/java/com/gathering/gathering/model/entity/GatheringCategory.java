package com.gathering.gathering.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GatheringCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_category")
    private Long id;
}
