package com.softserve.kh50project.davita.configuration;

import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserServiceImpl userServiceImpl;

    @Autowired
    public CustomUserDetailsService(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userServiceImpl.findByLogin(username);
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
