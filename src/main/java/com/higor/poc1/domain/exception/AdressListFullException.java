package com.higor.poc1.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdressListFullException extends RuntimeException {
    public AdressListFullException(String message){
        super(message);
    }
}
