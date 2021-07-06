package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "role_permission")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    Permission permission;
}
