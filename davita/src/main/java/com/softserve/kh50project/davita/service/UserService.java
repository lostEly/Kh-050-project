package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.UserDto;
import com.softserve.kh50project.davita.model.User;

import java.util.List;

public interface UserService {
    UserDto readById(long id);
    List<UserDto> getAll();

    UserDto create (UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    void delete(long id);

}
