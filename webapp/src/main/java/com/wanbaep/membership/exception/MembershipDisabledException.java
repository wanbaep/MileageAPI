package com.wanbaep.membership.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MembershipDisabledException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public MembershipDisabledException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
