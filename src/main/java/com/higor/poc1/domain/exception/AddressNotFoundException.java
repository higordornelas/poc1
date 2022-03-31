package com.higor.poc1.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message){
        super(message);
    }
}
