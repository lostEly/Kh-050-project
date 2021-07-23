package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "userr")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @Column(nullable = false)
    @Size(max = 45)
    String login;

    @Column(nullable = false)
    @Size(max = 25)
    String password;

    @Column(nullable = false)
    @Size(max = 45)
    String name;

    @Column(nullable = false)
    @Size(max = 45)
    String lastName;

    @Column
    LocalDate dateOfBirthday;

    @Column(unique = true)
    @Pattern(regexp = "(^$|[0-9]{10})")
    String phone;

    @Column(unique = true)
    @Email
    @Size(max = 45)
    String email;


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

}
