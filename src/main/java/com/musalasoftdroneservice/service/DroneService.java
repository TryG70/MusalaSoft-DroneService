package com.musalasoftdroneservice.service;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.reponse.APIResponse;

public interface DroneService {

    APIResponse<DroneDto> registerDrone(DroneDto droneDto);

    APIResponse<LoadedDroneDetails> loadDrone(DroneDto droneDto);

//    APIResponse<LoadedDroneDetails> getDroneMedicationItems(DroneDto droneDto);



}
