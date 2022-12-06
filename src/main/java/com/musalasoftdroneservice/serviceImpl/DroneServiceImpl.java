package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.exception.*;
import com.musalasoftdroneservice.mapper.DroneMapper;
import com.musalasoftdroneservice.mapper.MedicationMapper;
import com.musalasoftdroneservice.reponse.APIResponse;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;
import com.musalasoftdroneservice.service.DroneService;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    private final DroneMapper droneMapper;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository, MedicationMapper medicationMapper, DroneMapper droneMapper) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
        this.droneMapper = droneMapper;
    }

    @Override
    public APIResponse<DroneDto> registerDrone(DroneDto droneDto) {

        Optional<Drone> droneOptional = droneRepository.findBySerialNumber(droneDto.getSerialNumber());

        if(droneOptional.isEmpty()) {
            Drone drone = Drone.builder()
                    .serialNumber(droneDto.getSerialNumber())
                    .model(DroneModel.valueOf(droneDto.getModel()))
                    .weightLimit(droneDto.getWeightLimit())
                    .batteryCapacity((droneDto.getBatteryCapacity()))
                    .droneState(DroneState.IDLE)
                    .build();

            Drone newDrone = droneRepository.save(drone);
            DroneDto newDroneDto = droneMapper.droneToDroneDtoMapper(newDrone);

            return APIResponse.<DroneDto>builder()
                    .message("Drone registration successful")
                    .dto(newDroneDto)
                    .build();
        } else {
            throw new DroneAlreadyRegisteredException("Drone with serialNumber: " + droneDto.getSerialNumber() + " already registered");
        }
    }

    @Override
    @Transactional
    public APIResponse<LoadedDroneDetails> loadDrone(String droneSerialNumber, String medicationCode) {

        Drone drone = findDroneBySerialNumber(droneSerialNumber);

        if(drone.getBatteryCapacity() > 25) {
            Medication medication = findMedicationByCode(medicationCode);


            double totalWeight = medication.getWeight();

            for(Medication med : drone.getMedications()) {
                totalWeight += med.getWeight();
            }


            drone.setDroneState(DroneState.LOADING);
            droneRepository.save(drone);
            if(totalWeight < drone.getWeightLimit()) {

                drone.setDroneState(DroneState.LOADED);
                drone.getMedications().add(medication);

                droneRepository.save(drone);

                LoadedDroneDetails loadedDroneDetails = LoadedDroneDetails.builder()
                        .serialNumber(drone.getSerialNumber())
                        .medicationList(drone.getMedications())
                        .build();

                return APIResponse.<LoadedDroneDetails>builder()
                        .message("drone loaded successfully")
                        .dto(loadedDroneDetails)
                        .build();

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
    public APIResponse<List<MedicationDto>> getDroneMedicationItems(String serialNumber) {

        Drone drone = findDroneBySerialNumber(serialNumber);

        List<Medication> medicationList = drone.getMedications();

        List<MedicationDto> medicationDtoList = medicationList.stream().map(medicationMapper::medicationToMedicationDtoMapper).toList();

        return APIResponse.<List<MedicationDto>>builder()
                .message("Medication items found")
                .dto(medicationDtoList)
                .build();
    }

    @Override
    public APIResponse<List<DroneDto>> getAvailableDrones() {

        List<Drone> droneList = droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState.IDLE, 25)
                .orElseThrow(() -> new NoDronesAvailableException("No available drones at the moment"));


        List<DroneDto> droneDtoList = droneList.stream().map(droneMapper::droneToDroneDtoMapper).toList();

        return APIResponse.<List<DroneDto>>builder()
                .message("Available Drones found")
                .dto(droneDtoList)
                .build();
    }

    @Override
    public APIResponse<Integer> getDroneBatteryLevel(String serialNumber) {

        return APIResponse.<Integer>builder()
                .message("Battery Level fetched")
                .dto(droneRepository.getBatteryLevel(serialNumber).orElseThrow(() ->
                        new DroneNotFoundException("Drone with serialNumber: " + serialNumber + " was not found")))
                .build();
    }

    @Override
    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void periodicBatteryHealthCheck() {

        Logger logger = LoggerFactory.getLogger(DroneServiceImpl.class);

        List<Drone> drones = droneRepository.findAll();

        drones.forEach(drone ->  logger
                .info("Battery Level for Drone with serialNumber: " + drone.getSerialNumber() + " is " + drone.getBatteryCapacity()));
    }

    @Override
    public APIResponse<?> offloadDrone(String serialNumber) {

         Drone drone = findDroneBySerialNumber(serialNumber);

         drone.getMedications().removeAll(drone.getMedications());
         drone.setDroneState(DroneState.IDLE);

         droneRepository.save(drone);

            return APIResponse.builder()
                    .message("Drone unloaded successfully")
                    .build();
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
