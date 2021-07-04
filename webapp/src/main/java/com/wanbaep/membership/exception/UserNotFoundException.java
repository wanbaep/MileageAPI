package com.wanbaep.membership.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public UserNotFoundException(String message) {
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }
}
