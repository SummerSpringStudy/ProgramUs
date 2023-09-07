package com.pu.programus.exception;

import org.springframework.http.HttpStatus;

public class SignException extends PUException {
    public SignException(HttpStatus httpStatus, String message){
        super(httpStatus, message);
    }
}
