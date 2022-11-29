package com.musalasoftdroneservice.controller;

import com.musalasoftdroneservice.exception.DroneAlreadyRegistered;
import com.musalasoftdroneservice.reponse.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;


public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DroneAlreadyRegistered.class)
    public ResponseEntity<Object> orderNotFound(DroneAlreadyRegistered exception) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), FORBIDDEN);
        return new ResponseEntity<>(exceptionResponse, FORBIDDEN);
    }
}
