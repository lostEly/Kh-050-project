package com.softserve.kh50project.davita.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "userr_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
