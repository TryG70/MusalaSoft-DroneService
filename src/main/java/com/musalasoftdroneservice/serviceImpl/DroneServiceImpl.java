package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.exception.*;
import com.musalasoftdroneservice.reponse.APIResponse;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;
import com.musalasoftdroneservice.service.DroneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public APIResponse<LoadedDroneDetails> loadDrone(String serialNumber, String code) {

        Drone drone = findDroneBySerialNumber(serialNumber);

        if(drone.getBatteryCapacity().compareTo(new BigDecimal("0.25")) > 0) {
            Medication medication = findMedicationByCode(code);

            drone.getMedications().add(medication);

            double totalWeight = 0.0;

            for(Medication med : drone.getMedications()) {
                totalWeight += med.getWeight();
            }

            drone.setDroneState(DroneState.LOADING);
            droneRepository.save(drone);
            if(totalWeight < drone.getWeightLimit()) {

                drone.setDroneState(DroneState.LOADED);

                droneRepository.save(drone);

                LoadedDroneDetails loadedDroneDetails = LoadedDroneDetails.builder()
                        .serialNumber(drone.getSerialNumber())
                        .medicationList(drone.getMedications())
                        .build();

                return new APIResponse<>("drone loaded successfully", LocalDateTime.now(), loadedDroneDetails);

            } else {

                drone.setDroneState(DroneState.IDLE);
                droneRepository.save(drone);

                throw new DroneOverloadedException("The medications total weight, outweighs Drone with serialNumber: "
                        + drone.getSerialNumber() + " capacity of " + drone.getWeightLimit() + "gr");
            }
        } else  {
                throw  new DroneLowBatteryException("Drone Battery is Low");
        }
    }

    @Override
    public APIResponse<List<Medication>> getDroneMedicationItems(String serialNumber, LocalDateTime date) {

        Drone drone = findDroneBySerialNumber(serialNumber);

        return new APIResponse<>("medication items found", LocalDateTime.now(), medicationRepository.findAllByDrone_IdAndUpdatedAt(drone.getId(), date));
    }

    public Drone findDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber).orElseThrow(() ->
                new DroneNotFoundException("Drone with serialNumber: " + serialNumber + "not found in database"));
    }

    public Medication findMedicationByCode(String code) {
        return medicationRepository.findByCode(code).orElseThrow(() ->
                new MedicationNotFoundException("Medication with code: " + code + "not found in database"));
    }


}
