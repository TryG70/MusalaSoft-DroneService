package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class DroneLowBatteryException extends RuntimeException {

    public DroneLowBatteryException(String message) {
        super(message);
    }
}
