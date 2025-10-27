package com.example.identity_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    KEY_INVALID(2, "Invalid message key", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(3, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(4, "You do not have permission", HttpStatus.FORBIDDEN),

    USER_EXISTED(10, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(11, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(12, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(13, "User existed", HttpStatus.NOT_FOUND),
    INVALID_DOB(14, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    ROLE_NOT_EXISTED(20, "User existed", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
