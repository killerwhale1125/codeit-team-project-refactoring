package com.gathering.common.base.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, "SUCCESS", "Success"),

    // Auth
    AUTHORIZATION_FAIL(false, "AUTHORIZATION_FAIL", "Authorization Failed"),
    TOKEN_MISMATCHED(false, "TOKEN_MISMATCHED", "RefreshToken mismatched"),
    INVALID_TOKEN(false, "INVALID_TOKEN", "Invalid JWT Token"),
    EXPIRED_TOKEN(false, "EXPIRED_TOKEN", "Expired JWT Token"),
    UNSUPPORTED_TOKEN(false, "UNSUPPORTED_TOKEN", "Unsupported JWT Token"),
    TOKEN_ISEMPTY(false, "TOKEN_ISEMPTY", "JWT claims string is empty"),
    TOKEN_PARSE_ERROR(false, "TOKEN_PARSE_ERROR", "Unexpected JWT parsing error"),
    SIGN_IN_FAIL(false, "SIGN_IN_FAIL", "Login information mismatch"),
    ACCESS_DENIED(false, "ACCESS_DENIED", "You do not have permission to access this resource"),

    // User
    DUPLICATE_EMAIL(false, "DUPLICATE_EMAIL", "Duplicate email"),
    DUPLICATE_NICKNAME(false, "DUPLICATE_NICKNAME", "Duplicate nickname"),
    DUPLICATE_USERNAME(false, "DUPLICATE_USERNAME", "Duplicate username"),
    NOT_EXISTED_USER(false, "NON_EXISTED_USER", "This user does not exist"),
    DATABASE_ERROR(false, "DATABASE_ERROR", "Database error"),

    // Gathering
    NON_EXISTED_GATHERING(false, "NON_EXISTED_GATHERING", "This gathering does not exist"),
    EXCEEDS_CAPACITY(false, "EXCEEDS_CAPACITY", "The gathering exceeds the capacity limit"),
    ALREADY_JOINED(false, "ALREADY_JOINED", "You are already a member of this gathering"),
    GATHERING_ALREADY_STARTED(false, "ALREADY_JOINED", "The gathering has already started and no new participants can join"),
    GATHERING_ALREADY_ENDED(false, "ALREADY_JOINED", "The gathering has already ended and no new participants can join"),
    INVALID_GOAL_PERIOD(false, "INVALID_GOAL_PERIOD", "The goal period must be at least 1 day"),
    INVALID_MIN_CAPACITY(false, "INVALID_MIN_CAPACITY", "The minimum capacity must be at least 1"),
    INVALID_MAX_CAPACITY(false, "INVALID_MAX_CAPACITY", "The maximum capacity must be at least 1"),
    INVALID_CAPACITY_RANGE(false, "INVALID_CAPACITY_RANGE", "The maximum capacity must be greater than or equal to the minimum capacity"),
    BOOK_OR_CATEGORY_NOT_FOUND(false, "BOOK_OR_CATEGORY_NOT_FOUND", "The specified book or category could not be found"),

    // Other
    UNKNOWN_ERROR(false, "UNKNOWN_ERROR", "Unknown error"),
    UNSUPPORTED_FILETYPE(false, "UNSUPPORTED_FILETYPE", "Unsupported File Type!"),
    INVALID_REQUEST(false, "INVALID_REQUEST", "Invalid request parameters"),

    // Oauth
    SC_UNAUTHORIZED(false, "SC_UNAUTHORIZED", "Unauthorized: Missing or invalid JWT");

    private final boolean isSuccess;
    private final String code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, String code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
