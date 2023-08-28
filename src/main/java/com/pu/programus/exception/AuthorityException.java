package com.pu.programus.exception;

import org.springframework.http.HttpStatus;

public class AuthorityException extends PUException{

    public AuthorityException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
