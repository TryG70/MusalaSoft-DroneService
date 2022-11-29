package com.musalasoftdroneservice.entity;

import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drones")
public class Drone extends EntityBaseClass implements Serializable {

    @NotNull(message = "Serial Number is required")
    @Column(length = 100, unique = true)
    @Max(value = 100, message = "Serial Number should not be more than 100 characters")
    private String serialNumber;

    @Pattern(regexp = "(Lightweight|Middleweight|Cruiserweight|Heavyweight)", message = "Invalid model")
    @NotNull(message = "Drone Model is required")
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @DecimalMax(value = "500", message = "Weight limit is 500gr")
    @Digits(integer = 3, fraction = 2)
    @NotNull(message = "Weight Limit is required")
    private Double weightLimit;

    @NotNull(message = "Battery Capacity is required")
    @Pattern(regexp = "^(100|[1-9]?\\d)$", message = "Battery can not be more than 100%")
    private BigDecimal batteryCapacity;

    @NotNull(message = "Drone State is required")
    @Enumerated(EnumType.STRING)
    private DroneState droneState;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Medication> medications;
}
