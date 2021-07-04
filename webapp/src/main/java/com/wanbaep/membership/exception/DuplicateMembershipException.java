package com.wanbaep.membership.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateMembershipException extends RuntimeException{
    private final String message;
    private final HttpStatus status;

    public DuplicateMembershipException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
