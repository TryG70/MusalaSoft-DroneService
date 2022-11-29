package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class DroneNotFoundException extends RuntimeException{

    public DroneNotFoundException(String message) {
        super(message);
    }
}
