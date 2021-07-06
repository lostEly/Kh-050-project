package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "orderr")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long orderId;

    @Column(nullable = false)
    LocalDateTime start;

    @Column(nullable = false)
    LocalDateTime finish;

    @Column(nullable = false)
    Double cost;

    @ManyToOne(targetEntity = Procedure.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "procedure_id")
    Procedure procedure;

    @ManyToOne(targetEntity = Patient.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne(targetEntity = Doctor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    Doctor doctor;
}
