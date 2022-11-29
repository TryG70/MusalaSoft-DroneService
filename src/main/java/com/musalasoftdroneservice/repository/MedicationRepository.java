package com.musalasoftdroneservice.repository;

import com.musalasoftdroneservice.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    Optional<Medication> findByCode(String code);

    List<Medication> findAllByDrone_IdAndUpdatedAt(long id, LocalDateTime date);

}
