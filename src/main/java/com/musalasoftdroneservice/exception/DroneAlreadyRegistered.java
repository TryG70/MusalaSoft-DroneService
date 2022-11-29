package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class DroneAlreadyRegistered extends RuntimeException {

    public DroneAlreadyRegistered(String message) {
        super(message);
    }
}
