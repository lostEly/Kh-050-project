package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long roleId;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @OneToMany(mappedBy = "role")
    Set<RolePermission> rolePermissions;

    @OneToMany(mappedBy = "role")
    List<UserRole> userRoles;
}
