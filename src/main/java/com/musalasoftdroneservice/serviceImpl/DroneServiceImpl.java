package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.exception.DroneAlreadyRegisteredException;
import com.musalasoftdroneservice.exception.DroneNotFoundException;
import com.musalasoftdroneservice.exception.DroneOverloadedException;
import com.musalasoftdroneservice.reponse.APIResponse;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;
import com.musalasoftdroneservice.service.DroneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
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
                    .droneState(DroneState.IDLE)
                    .build();

            droneRepository.save(drone);

            return new APIResponse<>("Drone registration successful", LocalDateTime.now(), droneDto);
        } else {
            throw new DroneAlreadyRegisteredException("Drone with serialNumber: " + droneDto.getSerialNumber() + " already registered");
        }
    }

    @Override
    @Transactional
    public APIResponse<LoadedDroneDetails> loadDrone(DroneDto droneDto) {

        Drone drone = droneRepository.findBySerialNumber(droneDto.getSerialNumber()).orElseThrow(() ->
                new DroneNotFoundException("Drone with serialNumber: " + droneDto.getSerialNumber() + "not found in database"));

        double totalWeight = 0.0;

        for(Medication medication : droneDto.getMedications()) {
            totalWeight += medication.getWeight();
        }

        drone.setDroneState(DroneState.LOADING);
        droneRepository.save(drone);
        if(totalWeight < drone.getWeightLimit()) {

            drone.setDroneState(DroneState.LOADED);
            List<Medication> medicationList = medicationRepository.saveAll(droneDto.getMedications());

            drone.setMedications(medicationList);
            droneRepository.save(drone);

            LoadedDroneDetails loadedDroneDetails = LoadedDroneDetails.builder()
                    .serialNumber(droneDto.getSerialNumber())
                    .medicationList(droneDto.getMedications())
                    .build();

            return new APIResponse<>("drone loaded successfully", LocalDateTime.now(), loadedDroneDetails);

        } else {

            drone.setDroneState(DroneState.IDLE);
            droneRepository.save(drone);

            throw new DroneOverloadedException("The medications total weight, outweighs Drone with serialNumber: "
                    + droneDto.getSerialNumber() + " capacity of " + drone.getWeightLimit() + "gr");
        }
    }


}
