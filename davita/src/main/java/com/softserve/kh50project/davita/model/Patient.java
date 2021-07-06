package com.softserve.kh50project.davita.model;

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

    @Column(nullable = false)
    @Size(max = 45)
    String insuranceNumber;

    @Column
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    List<Order> orders;
}
