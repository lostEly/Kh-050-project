package com.softserve.kh50project.davita.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long permissionId;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @ManyToMany(mappedBy = "permissions")
    @ToString.Exclude
    private Set<Role> rolePermission = new HashSet<>();
}
