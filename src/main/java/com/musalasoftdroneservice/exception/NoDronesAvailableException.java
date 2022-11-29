package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class NoDronesAvailableException extends RuntimeException{

    public NoDronesAvailableException(String message) {
        super(message);
    }
}
