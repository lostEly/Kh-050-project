package com.softserve.kh50project.davita.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode
@Entity(name = "procedure")
@ToString
public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long procedureId;

    @Column(nullable = false)
    @Size(max = 45)
    private String name;

    @Column(nullable = false)
    private LocalTime duration;

    @Column(nullable = false)
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Equipment equipment;
}
