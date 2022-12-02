package com.musalasoftdroneservice.controller;


import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@Scope
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/musala-soft")
public class DroneController {

    private final DroneService droneService;

    @Operation(summary = "Register a new drone")
    @PostMapping(value = "/register/drone")
    public ResponseEntity<?> registerDrone(@Valid @RequestBody DroneDto droneDto, Errors error) {
        if(error.hasErrors()){
            return new ResponseEntity<>(Objects.requireNonNull(error.getFieldError()).getDefaultMessage(), BAD_REQUEST);
        }

        return new ResponseEntity<>(droneService.registerDrone(droneDto), CREATED);
    }

    @Operation(summary = "Drone loading")
    @PostMapping(value = "/load/drone/{serialNumber}/{code}")
    public ResponseEntity<?> loadDrone(@PathVariable(value = "serialNumber") String serialNumber, @PathVariable(value = "code") String code) {
        return new ResponseEntity<>(droneService.loadDrone(serialNumber, code), OK);
    }

    @Operation(summary = "Get drone loaded medication items")
    @GetMapping(value = "/drone/medication-items/{serialNumber}")
    public ResponseEntity<?> getDroneMedicationItems(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.getDroneMedicationItems(serialNumber), OK);
    }

    @Operation(summary = "Get all available drones(Drones with the state of the drone as Idle and Battery level greater than 25%)")
    @GetMapping(value = "/available-drone")
    public ResponseEntity<?> getAvailableDrones(){
        return new ResponseEntity<>(droneService.getAvailableDrones(), OK);
    }

    @Operation(summary = "Get drone battery level")
    @GetMapping(value = "/drone/battery-level/{serialNumber}")
    public ResponseEntity<?> getDroneBatteryLevel(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.getDroneBatteryLevel(serialNumber), OK);
    }

    @Operation(summary = "Offload drone medications")
    @GetMapping(value = "/offload-drone/{serialNumber}")
    public ResponseEntity<?> offloadDrone(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.offloadDrone(serialNumber), OK);
    }
}
