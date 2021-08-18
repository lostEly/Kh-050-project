package com.softserve.kh50project.davita.controller;
import com.softserve.kh50project.davita.config.jwt.JwtProvider;
import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private UserService userService;
    private JwtProvider jwtProvider;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register") //signup
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        try {
            userService.saveUser(u);
        } catch (Exception e) {
            return new ResponseEntity<>("User with such login already exists!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("You are successfully registered!", HttpStatus.OK);
    }

    @PostMapping("/authorize")      //signin
    public ResponseEntity<?> authorize(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if (user == null) {
            return new ResponseEntity<>("Bad Credentials!", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtProvider.generateToken(user.getLogin());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

}
