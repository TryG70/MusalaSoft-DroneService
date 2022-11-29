package com.musalasoftdroneservice.controller;

import com.musalasoftdroneservice.exception.*;
import com.musalasoftdroneservice.reponse.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;


@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DroneAlreadyRegisteredException.class)
    public ResponseEntity<Object> orderNotFound(DroneAlreadyRegisteredException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), FORBIDDEN);
        return new ResponseEntity<>(exceptionResponse, FORBIDDEN);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<Object> orderNotFound(DroneNotFoundException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }

    @ExceptionHandler(DroneOverloadedException.class)
    public ResponseEntity<Object> orderNotFound(DroneOverloadedException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_ACCEPTABLE);
        return new ResponseEntity<>(exceptionResponse, NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MedicationNotFoundException.class)
    public ResponseEntity<Object> orderNotFound(MedicationNotFoundException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }

    @ExceptionHandler(DroneLowBatteryException.class)
    public ResponseEntity<Object> orderNotFound(DroneLowBatteryException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), PRECONDITION_FAILED);
        return new ResponseEntity<>(exceptionResponse, PRECONDITION_FAILED);
    }

    @ExceptionHandler(NoDronesAvailableException.class)
    public ResponseEntity<Object> orderNotFound(NoDronesAvailableException exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, NOT_FOUND);
    }

}
