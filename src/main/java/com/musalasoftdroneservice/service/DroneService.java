package com.musalasoftdroneservice.service;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.reponse.APIResponse;

import java.math.BigDecimal;
import java.util.List;

public interface DroneService {

    APIResponse<DroneDto> registerDrone(DroneDto droneDto);

    APIResponse<LoadedDroneDetails> loadDrone(String serialNumber, String code);

    APIResponse<List<MedicationDto>> getDroneMedicationItems(String serialNumber);

    APIResponse<List<DroneDto>> getAvailableDrones();

    APIResponse<BigDecimal> getDroneBatteryLevel(String serialNumber);

    APIResponse<?> periodicBatteryHealthCheck(List<Drone> drones);

    APIResponse<?> offloadDrone(String serialNumber);

}
