package com.ceylon_adds.system_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SMSGatewayException extends RuntimeException {
    public SMSGatewayException(String message) {
        super(message);
    }
}
