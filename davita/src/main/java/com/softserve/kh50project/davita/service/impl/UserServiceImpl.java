package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.UserDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Role;
import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.repository.RoleRepository;
import com.softserve.kh50project.davita.repository.UserRepository;
import com.softserve.kh50project.davita.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDto readById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("User with id=" + id + " does not exist!")));
        return convertUserToDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());
    }

//    public User saveUser(User user) {
//        Role userRole = roleRepository.findByName("ROLE_USER");
//        user.addRole(userRole);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }


    @Override
    public UserDto create(UserDto userDto) {
        User createdUser = convertDtoToUser(userDto);
        userRepository.save(createdUser);
        return convertUserToDto(createdUser);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        userDto.setUserId(id);
        User user = convertDtoToUser(userDto);
        userRepository.save(user);
        return convertUserToDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
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


    private UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
