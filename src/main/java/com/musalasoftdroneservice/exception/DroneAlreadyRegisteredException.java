package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class DroneAlreadyRegisteredException extends RuntimeException {

    public DroneAlreadyRegisteredException(String message) {
        super(message);
    }
}
