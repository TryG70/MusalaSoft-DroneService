package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class DroneOverloadedException extends RuntimeException{

    public DroneOverloadedException(String message) {
        super(message);
    }
}
