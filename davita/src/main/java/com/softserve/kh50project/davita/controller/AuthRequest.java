package com.softserve.kh50project.davita.controller;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
