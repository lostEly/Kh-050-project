package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @OneToMany(mappedBy = "permission")
    Set<RolePermission> rolePermissions;
}
