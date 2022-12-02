package com.musalasoftdroneservice.mapper;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.entity.Drone;
import org.springframework.stereotype.Service;

@Service
public class DroneMapper {

    public DroneDto droneToDroneDtoMapper(Drone drone) {

        return DroneDto.builder()
                .serialNumber(drone.getSerialNumber())
                .model(String.valueOf(drone.getModel()))
                .weightLimit(drone.getWeightLimit())
                .batteryCapacity(drone.getBatteryCapacity())
                .droneState(drone.getDroneState())
                .build();
    }
}
