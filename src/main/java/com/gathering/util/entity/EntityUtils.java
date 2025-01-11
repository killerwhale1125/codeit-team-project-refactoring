package com.gathering.util.entity;

import java.util.function.Function;

public class EntityUtils {
    private EntityUtils() {
        // Utility class, no need to instantiate
    }

    public static <T, R> R nullableEntity(Function<T, R> mapper, T entity) {
        return entity == null ? null : mapper.apply(entity);
    }
}
