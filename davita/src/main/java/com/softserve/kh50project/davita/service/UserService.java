package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.User;

import java.util.List;

public interface UserService {
    User create (User user);
    User readById(long id);
    User update(User user, Long id);
    void delete(long id);
    List<User> getAll();
}
