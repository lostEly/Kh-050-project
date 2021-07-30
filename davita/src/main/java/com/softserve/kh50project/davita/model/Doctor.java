package com.softserve.kh50project.davita.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "doctor")
@PrimaryKeyJoinColumn(name = "doctor_id")
public class Doctor extends User {

    @Column(nullable = false)
    @Size(max = 45)
    private String specialization;

    @Column(nullable = false)
    @Size(max = 45)
    private String certificateNumber;

    @ToString.Exclude
    @Column
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Order> orders;
}
