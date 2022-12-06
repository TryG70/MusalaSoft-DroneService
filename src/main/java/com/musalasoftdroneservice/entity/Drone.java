package com.musalasoftdroneservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Drone drone = (Drone) o;
        return getId() != 0 && Objects.equals(getId(), drone.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
