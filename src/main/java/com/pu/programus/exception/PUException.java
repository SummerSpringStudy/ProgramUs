package com.pu.programus.exception;

import org.springframework.http.HttpStatus;

public class PUException extends Exception {

    private HttpStatus httpStatus;
    public PUException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpStatusType() {
        return httpStatus.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
