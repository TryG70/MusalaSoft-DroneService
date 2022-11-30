package com.musalasoftdroneservice.controller;


import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.service.DroneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Scope
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/musala-soft")
public class DroneController {

    private final DroneService droneService;

    @PostMapping(value = "/register/drone")
    public ResponseEntity<?> registerDrone(@RequestBody DroneDto droneDto) {
        return new ResponseEntity<>(droneService.registerDrone(droneDto), CREATED);
    }

    @PostMapping(value = "/load/drone/{serialNumber}/{code}")
    public ResponseEntity<?> loadDrone(@PathVariable(value = "serialNumber") String serialNumber, @PathVariable(value = "code") String code) {
        return new ResponseEntity<>(droneService.loadDrone(serialNumber, code), OK);
    }

    @GetMapping(value = "/drone/medication-items/{serialNumber}/{time}")
    public ResponseEntity<?> getDroneMedicationItems(@PathVariable(value = "serialNumber") String serialNumber, @PathVariable(value = "time") LocalDateTime time) {
        return new ResponseEntity<>(droneService.getDroneMedicationItems(serialNumber, time), FOUND);
    }

    @GetMapping(value = "/available-drone")
    public ResponseEntity<?> getAvailableDrones(){
        return new ResponseEntity<>(droneService.getAvailableDrones(), FOUND);
    }

    @GetMapping(value = "/drone/battery-level/{serialNumber}")
    public ResponseEntity<?> getDroneBatteryLevel(@PathVariable(value = "serialNumber") String serialNumber) {
        return new ResponseEntity<>(droneService.getDroneBatteryLevel(serialNumber), FOUND);
    }


}
