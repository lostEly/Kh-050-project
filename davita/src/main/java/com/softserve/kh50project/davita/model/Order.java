package com.softserve.kh50project.davita.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Data
@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "orderr")
@ToString
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

    @ManyToOne(targetEntity = Procedure.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id")
    Procedure procedure;

    @ManyToOne(targetEntity = Patient.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @ManyToOne(targetEntity = Doctor.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

}
