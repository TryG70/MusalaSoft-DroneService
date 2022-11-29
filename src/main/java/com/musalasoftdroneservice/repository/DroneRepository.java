package com.musalasoftdroneservice.repository;

import com.musalasoftdroneservice.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, Long> {
}
