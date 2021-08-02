package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.PermissionDto;
import com.softserve.kh50project.davita.model.Permission;

import java.util.List;

public interface PermissionService {
    PermissionDto create(PermissionDto permissionDto);
    PermissionDto readById(long id);
    PermissionDto update(PermissionDto permissionDto, long id);
    void delete(long id);
    List<PermissionDto> getAll();
}
