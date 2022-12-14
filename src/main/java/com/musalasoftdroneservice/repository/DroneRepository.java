package com.musalasoftdroneservice.repository;

import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    Optional<Drone> findBySerialNumber(String serialNumber);

    Optional<List<Drone>> findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState droneState, int batteryPercentage);

    @Query(value = "SELECT battery_Capacity FROM drones WHERE serial_Number = ?1", nativeQuery = true)
    Optional<Integer> getBatteryLevel(String serialNumber);

}
