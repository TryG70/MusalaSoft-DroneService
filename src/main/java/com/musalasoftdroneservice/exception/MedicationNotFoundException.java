package com.musalasoftdroneservice.exception;

import lombok.Data;

@Data
public class MedicationNotFoundException extends RuntimeException {

    public MedicationNotFoundException(String message) {
        super(message);
    }
}
