package com.softserve.kh50project.davita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping("")
    public String getIndex() {
        return "index";
    }
}
