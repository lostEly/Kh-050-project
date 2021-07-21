package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.model.Role;
import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.model.UserRole;
import com.softserve.kh50project.davita.repository.RoleRepository;
import com.softserve.kh50project.davita.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");             ////???????
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRoles(new ArrayList<UserRole>().add(userRole));        ////???????
        return userRepository.save(user);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
