package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity(name = "procedure")
public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long procedureId;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @Column(nullable = false)
    LocalTime duration;

    @Column(nullable = false)
    Double cost;

    @ManyToOne(targetEntity = Equipment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id", nullable = false)
    Equipment equipment;
}
