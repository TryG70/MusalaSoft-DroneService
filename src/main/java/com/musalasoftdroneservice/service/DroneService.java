package com.musalasoftdroneservice.service;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.reponse.APIResponse;

public interface DroneService {

    APIResponse<DroneDto> registerDrone(DroneDto droneDto);

}