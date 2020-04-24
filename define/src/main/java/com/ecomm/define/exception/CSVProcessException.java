package com.ecomm.define.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CSVProcessException extends RuntimeException
{
    public CSVProcessException(String exception) {
        super(exception);
    }
}