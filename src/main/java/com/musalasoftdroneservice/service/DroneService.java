package com.musalasoftdroneservice.service;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.reponse.APIResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface DroneService {

    APIResponse<DroneDto> registerDrone(DroneDto droneDto);

    APIResponse<LoadedDroneDetails> loadDrone(String serialNumber, String code);

    APIResponse<List<MedicationDto>> getDroneMedicationItems(String serialNumber, LocalDateTime date);

    APIResponse<List<DroneDto>> getAvailableDrones();

}
