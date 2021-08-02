package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.configuration.jwt.JwtFilter;
import com.softserve.kh50project.davita.configuration.jwt.JwtProvider;
import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private UserServiceImpl userServiceImpl;
    private JwtProvider jwtProviderl;


    @Autowired
    public void setUserServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register") //signup
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        userServiceImpl.saveUser(u);
        return "OK";
    }

    @PostMapping("/auth")      //signin
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userServiceImpl.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }
}
