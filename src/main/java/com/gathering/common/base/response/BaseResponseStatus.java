package com.gathering.common.base.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.OK, "SUCCESS", "Success"),

    // Auth
    AUTHORIZATION_FAIL(HttpStatus.UNAUTHORIZED, "AUTHORIZATION_FAIL", "Authorization Failed"),
    TOKEN_MISMATCHED(HttpStatus.UNAUTHORIZED, "TOKEN_MISMATCHED", "RefreshToken mismatched"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid JWT Token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "Expired JWT Token"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "UNSUPPORTED_TOKEN", "Unsupported JWT Token"),
    TOKEN_ISEMPTY(HttpStatus.BAD_REQUEST, "TOKEN_ISEMPTY", "JWT claims string is empty"),
    TOKEN_PARSE_ERROR(HttpStatus.BAD_REQUEST, "TOKEN_PARSE_ERROR", "Unexpected JWT parsing error"),
    SIGN_IN_FAIL(HttpStatus.UNAUTHORIZED, "SIGN_IN_FAIL", "Login information mismatch"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "You do not have permission to access this resource"),

    // User
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "Duplicate email"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "DUPLICATE_NICKNAME", "Duplicate nickname"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "DUPLICATE_USERNAME", "Duplicate username"),
    NOT_EXISTED_USER(HttpStatus.NOT_FOUND, "NON_EXISTED_USER", "This user does not exist"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "Database error"),
    EDIT_USER_FAIL(HttpStatus.BAD_REQUEST, "EDIT_USER_FAIL", "Edit user fail"),
    PASSWORD_MISMATCHED(HttpStatus.UNAUTHORIZED, "PASSWORD_MISMATCHED", "Password mismatched"),
    // Gathering & CHALLENGE
    NON_EXISTED_GATHERING(HttpStatus.NOT_FOUND, "NON_EXISTED_GATHERING", "This gathering does not exist"),
    NON_EXISTED_CHALLENGE(HttpStatus.NOT_FOUND, "NON_EXISTED_CHALLENGE", "This challenge does not exist"),
    EXCEEDS_CAPACITY(HttpStatus.BAD_REQUEST, "EXCEEDS_CAPACITY", "The gathering exceeds the capacity limit"),
    ALREADY_JOINED(HttpStatus.CONFLICT, "ALREADY_JOINED", "You are already a member of this gathering"),
    GATHERING_FULL(HttpStatus.BAD_REQUEST, "GATHERING_FULL", "The gathering has reached its maximum capacity and cannot accept new participants."),
    GATHERING_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "GATHERING_ALREADY_STARTED", "The gathering has already started."),
    GATHERING_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "GATHERING_ALREADY_DELETED", "The gathering has been deleted"),
    GATHERING_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "GATHERING_ALREADY_ENDED", "The gathering has already ended"),
    USER_NOT_IN_GATHERING(HttpStatus.BAD_REQUEST, "USER_NOT_IN_GATHERING", "User is not part of the gathering."),
    USER_NOT_IN_CHALLENGE(HttpStatus.BAD_REQUEST, "USER_NOT_IN_CHALLENGE", "User is not part of the challenge."),
    HOST_CANNOT_LEAVE_GATHERING(HttpStatus.BAD_REQUEST, "HOST_CANNOT_LEAVE_GATHERING", "The host cannot leave the gathering."),
    INVALID_GOAL_PERIOD(HttpStatus.BAD_REQUEST, "INVALID_GOAL_PERIOD", "The goal period must be at least 1 day"),
    INVALID_MIN_CAPACITY(HttpStatus.BAD_REQUEST, "INVALID_MIN_CAPACITY", "The minimum capacity must be at least 5"),
    INVALID_MAX_CAPACITY(HttpStatus.BAD_REQUEST, "INVALID_MAX_CAPACITY", "The maximum capacity must be at least 6"),
    INVALID_CAPACITY_RANGE(HttpStatus.BAD_REQUEST, "INVALID_CAPACITY_RANGE", "The maximum capacity must be greater than or equal to the minimum capacity"),
    INVALID_CHALLENGE_START_BETWEEN_TIME(HttpStatus.BAD_REQUEST, "INVALID_CHALLENGE_START_BETWEEN_TIME", "Challenge start between time must be greater than 0"),
    // Book & CATEGORY
    BOOK_OR_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_OR_CATEGORY_NOT_FOUND", "The specified book or category could not be found"),
    NOT_EXISTED_BOOK(HttpStatus.NOT_FOUND, "NOT_EXISTED_BOOK", "This book does not exist"),
    INVALID_SEARCH_WORD(HttpStatus.BAD_REQUEST, "INVALID_SEARCH_WORD", "The search keyword must be at least 3 characters long"),
    // review
    NON_EXISTED_REVIEW(HttpStatus.NOT_FOUND, "NON_EXISTED_REVIEW", "This review does not exist"),

    // Other
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_ERROR", "Unknown error"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Invalid request parameters"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal Server Error!"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Bad Request"),

    // File
    UNSUPPORTED_FILETYPE(HttpStatus.BAD_REQUEST, "UNSUPPORTED_FILETYPE", "Unsupported File Type!"),
    NON_EXISTED_IMAGE(HttpStatus.NOT_FOUND, "IMAGE_NOT_FOUND", "This image does not exist"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_UPLOAD_FAILED", "File upload failed"),

    // Oauth
    SC_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SC_UNAUTHORIZED", "Unauthorized: Missing or invalid JWT"),

    // Redis
    REDIS_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_OPERATION_FAILED", "Failed to save key to Redis");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    BaseResponseStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Schema(description = "Base response status")
    public String getMessage() {
        return message;
    }

    @Schema(description = "Base response code")
    public String getCode() {
        return code;
    }
}
