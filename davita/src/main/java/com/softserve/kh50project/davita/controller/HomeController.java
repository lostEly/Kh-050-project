package com.softserve.kh50project.davita.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class HomeController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/", "home"})
    public String getHome() {
        return "home";
    }
}
