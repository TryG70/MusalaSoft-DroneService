package com.musalasoftdroneservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drones")
@Entity
public class Drone extends EntityBaseClass implements Serializable {

    @Column(length = 100, unique = true)
    private String serialNumber;


    @Enumerated(EnumType.STRING)
    private DroneModel model;


    private Double weightLimit;


    private int batteryCapacity;


    @Enumerated(EnumType.STRING)
    private DroneState droneState;


    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id", referencedColumnName = "id")
    private List<Medication> medications;
}
