package com.softserve.kh50project.davita.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "orderr")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    @Column(nullable = false)
    LocalDateTime start;

    @Column(nullable = false)
    LocalDateTime finish;

    @Column(nullable = false)
    Double cost;

    @JsonBackReference
    @ManyToOne(targetEntity = Procedure.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "procedure_id")
    Procedure procedure;

    @JsonBackReference
    @ManyToOne(targetEntity = Patient.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @JsonBackReference
    @ManyToOne(targetEntity = Doctor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    Doctor doctor;
}
