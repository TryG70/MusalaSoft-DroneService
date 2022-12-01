package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public APIResponse<List<MedicationDto>> getDroneMedicationItems(String serialNumber, LocalDateTime date) {

        Drone drone = findDroneBySerialNumber(serialNumber);


        List<MedicationDto> medicationDtoList = new ArrayList<>();
        List<Medication> medicationList = medicationRepository.findAllByDrone_IdAndUpdatedAt(drone.getId(), date);


        medicationList.forEach(medication -> {
            MedicationDto medicationDto = medicationMapper.medicationToMedicationDtoMapper(medication);
            medicationDtoList.add(medicationDto);

        });

        return new APIResponse<>("Medication items found", LocalDateTime.now(), medicationDtoList);
    }

    @Override
    public APIResponse<List<DroneDto>> getAvailableDrones() {

        List<DroneDto> droneDtoList = new ArrayList<>();

        List<Drone> droneList = droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState.IDLE, BigDecimal.valueOf(0.25))
                .orElseThrow(() -> new NoDronesAvailableException("No available drones at the moment"));

        droneList.forEach(drone -> {
            DroneDto droneDto = droneMapper.droneToDroneDtoMapper(drone);
            droneDtoList.add(droneDto);
        });

        return new APIResponse<>("Available Drones found", LocalDateTime.now(), droneDtoList);
    }

    @Override
    public APIResponse<BigDecimal> getDroneBatteryLevel(String serialNumber) {

        return new APIResponse<>("Battery Level fetched", LocalDateTime.now(),
                droneRepository.getBatteryLevel(serialNumber).orElseThrow(() ->
                        new DroneNotFoundException("Drone with serialNumber: " + serialNumber + " was not found")));
    }

    @Override
    public APIResponse<?> periodicBatteryHealthCheck(List<Drone> drones) {

        Logger logger = LoggerFactory.getLogger(DroneServiceImpl.class);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                drones.forEach(drone ->  logger
                        .info("Battery Level for Drone with serialNumber: " + drone.getSerialNumber() + " is " + drone.getBatteryCapacity()));
            }
        } ,1000, 500000);

        return APIResponse.builder()
                .message("Drone battery health checked")
                .time(LocalDateTime.now())
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
