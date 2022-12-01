package com.musalasoftdroneservice.entity;



import lombok.*;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medications")
public class Medication extends EntityBaseClass{


    private String name;


    private Double weight;


    private String code;


    private String image;

}
