package com.musalasoftdroneservice;

import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class MusalaSoftDroneServiceApplication implements CommandLineRunner {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private static final Logger logger = LoggerFactory.getLogger(MusalaSoftDroneServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MusalaSoftDroneServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        droneRepository.save(Drone.builder().serialNumber("HV5920434380").model(DroneModel.Lightweight).weightLimit(385.7).batteryCapacity(75).droneState(DroneState.IDLE).build());
        droneRepository.save(Drone.builder().serialNumber("MQ5920434485").model(DroneModel.Middleweight).weightLimit(205.2).batteryCapacity(45).droneState(DroneState.LOADED).build());
        droneRepository.save(Drone.builder().serialNumber("LN5920436382").model(DroneModel.Cruiserweight).weightLimit(400.0).batteryCapacity(98).droneState(DroneState.IDLE).build());
        droneRepository.save(Drone.builder().serialNumber("ER59206734383").model(DroneModel.Heavyweight).weightLimit(189.9).batteryCapacity(69).droneState(DroneState.IDLE).build());

        logger.info("Drones registered successfully");


        medicationRepository.save(Medication.builder().name("Doxycycline").weight(0.41).code("ED7942").image("droneHV.jpeg").build());
        medicationRepository.save(Medication.builder().name("Prima quine").weight(0.10).code("MM7943").image("droneMQ.jpeg").build());
        medicationRepository.save(Medication.builder().name("Robitussin").weight(0.25).code("QS7944").image("droneLN.jpeg").build());

        logger.info("Medications registered successfully");
    }
}
