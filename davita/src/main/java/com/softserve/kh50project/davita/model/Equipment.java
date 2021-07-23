package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long equipmentId;

    @Column(nullable = false)
    String name;

    @Column
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    List<Procedure> procedures;
}