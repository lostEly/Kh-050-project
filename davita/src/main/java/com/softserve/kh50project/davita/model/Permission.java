package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long permissionId;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @ManyToMany(mappedBy = "permission")
    private Set<Role> rolePermission = new HashSet<>();
}
