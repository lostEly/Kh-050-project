package com.softserve.kh50project.davita.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PatientDto {

    private Long userId;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private LocalDate dateOfBirthday;
    private String phone;
    private String email;
    private String insuranceNumber;

}
