package com.ecomm.define.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateRecordFoundException extends RuntimeException
{
    public DuplicateRecordFoundException(String exception) {
        super(exception);
    }
}