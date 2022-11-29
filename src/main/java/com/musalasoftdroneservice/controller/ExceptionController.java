package com.musalasoftdroneservice.controller;

import com.musalasoftdroneservice.exception.DroneAlreadyRegisteredException;
import com.musalasoftdroneservice.exception.DroneNotFoundException;
import com.musalasoftdroneservice.reponse.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;


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
}
