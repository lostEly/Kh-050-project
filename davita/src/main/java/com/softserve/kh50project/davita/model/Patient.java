package com.softserve.kh50project.davita.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@PrimaryKeyJoinColumn(name = "patient_id")
public class Patient extends User {

    @Column(nullable = false, unique = true)
    @Size(max = 45)
    private String insuranceNumber;

    @JsonManagedReference
    @Column
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Order> orders;
}
