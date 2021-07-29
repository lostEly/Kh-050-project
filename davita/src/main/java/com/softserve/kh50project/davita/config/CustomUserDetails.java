package com.softserve.kh50project.davita.config;

import com.softserve.kh50project.davita.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;  ///56 min

    public static CustomUserDetails fromUserToCustomUserDetails(User user) {
        CustomUserDetails c = new CustomUserDetails();
        c.login = user.getLogin();
        c.password = user.getPassword();
       // c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRoles().getName()));   ///??List of roles
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
