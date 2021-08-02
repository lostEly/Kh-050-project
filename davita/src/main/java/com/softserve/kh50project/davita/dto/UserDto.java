package com.softserve.kh50project.davita.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserDto {
    private Long userId;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private LocalDate dateOfBirthday;
    private String phone;
    private String email;
}
