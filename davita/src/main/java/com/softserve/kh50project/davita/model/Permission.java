package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity()
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long permissionId;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> rolePermission = new HashSet<>();
}
