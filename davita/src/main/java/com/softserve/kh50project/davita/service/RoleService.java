package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Role;

import java.util.List;

public interface RoleService {
    Role create(Role role);
    Role readById(long id);
    Role update(Role role, long id);
    void delete(long id);
    List<Role> getAll();
}
