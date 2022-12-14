package com.musalasoftdroneservice.controller;

import com.musalasoftdroneservice.exception.*;
import com.musalasoftdroneservice.reponse.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DroneAlreadyRegisteredException.class)
    public ResponseEntity<Object> droneAlreadyRegistered(DroneAlreadyRegisteredException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), FORBIDDEN);
        return new ResponseEntity<>(exceptionResponse, FORBIDDEN);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<Object> droneNotFound(DroneNotFoundException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }

    @ExceptionHandler(DroneOverloadedException.class)
    public ResponseEntity<Object> droneOverloaded(DroneOverloadedException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_ACCEPTABLE);
        return new ResponseEntity<>(exceptionResponse, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<Object> medicationNotFound(MedicationNotFoundException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }

    @ExceptionHandler(DroneLowBatteryException.class)
    public ResponseEntity<Object> droneLowBattery(DroneLowBatteryException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), PRECONDITION_FAILED);
        return new ResponseEntity<>(exceptionResponse, PRECONDITION_FAILED);
    }

    @ExceptionHandler(NoDronesAvailableException.class)
    public ResponseEntity<Object> noDronesAvailable(NoDronesAvailableException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }
}
