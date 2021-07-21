package com.softserve.kh50project.davita.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity(name = "userr")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User   {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    @Column
    @Pattern(regexp = "(^$|[0-9]{10})")
    String phone;

    @Column
    @Email
    @Size(max = 45)
    String email;

    @OneToMany(mappedBy = "user")
    List<UserRole> userRoles;

}
