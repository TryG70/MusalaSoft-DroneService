package com.musalasoftdroneservice.dto;

import com.musalasoftdroneservice.enums.DroneState;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Builder
@Data
public class DroneDto {


    @NotNull(message = "Serial Number is required")
    @Size(max = 100, message = "Serial Number should not be more than 100 characters")
    private String serialNumber;

    @Pattern(regexp = "(Lightweight|Middleweight|Cruiserweight|Heavyweight)", message = "Invalid model")
    @NotNull(message = "Drone Model is required")
    private String model;


    @DecimalMax(value = "500", message = "Weight limit is 500gr")
    @Digits(integer = 3, fraction = 2)
    @NotNull(message = "Weight Limit is required")
    private Double weightLimit;


    @NotNull(message = "Battery Capacity is required")
    @Max(value = 100, message = "Battery can not be more than 100%")
    private int batteryCapacity;


    @NotNull(message = "Drone State is required")
    private DroneState droneState;

}
