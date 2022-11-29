package com.musalasoftdroneservice.mapper;

import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Medication;
import org.springframework.stereotype.Service;

@Service
public class MedicationMapper {

    public MedicationDto medicationToMedicationDtoMapper(Medication medication) {
        return MedicationDto.builder()
                .name(medication.getName())
                .weight(medication.getWeight())
                .code(medication.getCode())
                .image(medication.getImage())
                .build();
    }
}
