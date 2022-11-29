package com.musalasoftdroneservice.dto;

import com.musalasoftdroneservice.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class LoadedDroneDetails {

    private String serialNumber;

    private List<Medication> medicationList;
}
