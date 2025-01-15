package com.gathering.image.infrastructure.entity;

public enum EntityType {
    GATHERING("gathering"),
    USER("user");

    private final String entityType;

    EntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return entityType;
    }
}
