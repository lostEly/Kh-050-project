package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
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

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    public void addPermission(Permission permission) {
        permissions.add(permission);
        permission.getRolePermission().add(this);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
        permission.getRolePermission().remove(this);
    }

}
