//package com.softserve.kh50project.davita.security;
//
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class LoginController {
//    @GetMapping("login-form")
//    public String login() {
//        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
//            return "redirect:/home";
//        }
//        return "login";
//    }
//}
