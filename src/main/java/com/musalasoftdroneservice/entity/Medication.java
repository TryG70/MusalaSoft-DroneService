package com.musalasoftdroneservice.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medications")
public class Medication extends EntityBaseClass{

    @Pattern(regexp = "(^[a-zA-Z0-9-_])$", message = "letters, numbers, hyphen and underscore allowed")
    @NotNull(message = "Name of medication is required")
    private String name;

    @NotNull(message = "Weight of medication is required")
    private Double weight;

    @Pattern(regexp = "(^[A-Z0-9_])$", message = "upper case letters, numbers and underscore allowed")
    @NotNull(message = "Code of medication is required")
    private String code;

    private String image;
}
