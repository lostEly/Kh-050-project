package com.softserve.kh50project.davita.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "equipment")
@ToString
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long equipmentId;

    @Column(nullable = false)
    String name;

    @OneToMany(
            mappedBy = "equipment",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    @ToString.Exclude
    List<Procedure> procedures = new ArrayList<>();

    public void addProcedure(Procedure procedure) {
        procedures.add(procedure);
        procedure.setEquipment(this);
    }

    public void removeProcedure(Procedure procedure) {
        procedures.remove(procedure);
        procedure.setEquipment(null);
    }
}
