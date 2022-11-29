package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.exception.DroneAlreadyRegistered;
import com.musalasoftdroneservice.reponse.APIResponse;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public APIResponse<DroneDto> registerDrone(DroneDto droneDto) {

        Optional<Drone> droneOptional = droneRepository.findBySerialNumber(droneDto.getSerialNumber());

        if(droneOptional.isEmpty()) {
            Drone drone = Drone.builder()
                    .serialNumber(droneDto.getSerialNumber())
                    .model(droneDto.getModel())
                    .weightLimit(droneDto.getWeightLimit())
                    .batteryCapacity(droneDto.getBatteryCapacity())
                    .droneState(droneDto.getDroneState())
                    .build();

            droneRepository.save(drone);

            return new APIResponse<>("Drone registration successful", LocalDateTime.now(), droneDto);
        } else {
            throw new DroneAlreadyRegistered("Drone with serialNumber: " + droneDto.getSerialNumber() + " already registered");
        }
    }
}
