package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Permission;

import java.util.List;

public interface PermissionService {
    Permission create(Permission permission);
    Permission readById(long id);
    Permission update(Permission permission, long id);
    void delete(long id);
    List<Permission> getAll();
}
