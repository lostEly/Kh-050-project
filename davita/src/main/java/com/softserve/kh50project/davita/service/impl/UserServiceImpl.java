package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.model.User;
import com.softserve.kh50project.davita.repository.UserRepository;
import com.softserve.kh50project.davita.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    //TODO change NoSuchElementException to EntityNotFoundException
    @Override
    public User readById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(("User with id=" + id + " does not exist!")));
    }

    //TODO change NoSuchElementException to EntityNotFoundException
    @Override
    public User update(User user, Long id) {
        Optional.ofNullable(userRepository.findByLogin(user.getEmail()))
                .orElseThrow(() -> new NoSuchElementException("User with email = " + user.getEmail() + " does not exist!"));

        return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }
}
