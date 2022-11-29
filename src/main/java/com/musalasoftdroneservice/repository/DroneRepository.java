package com.musalasoftdroneservice.repository;

import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    Optional<Drone> findBySerialNumber(String serialNumber);

    Optional<List<Drone>> findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState droneState, BigDecimal batteryPercentage);
}
