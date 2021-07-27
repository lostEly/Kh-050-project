package com.softserve.kh50project.davita.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @JsonManagedReference
    @Column
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Order> orders;
}
