package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.RoleDto;
import com.softserve.kh50project.davita.model.Role;

import java.util.List;

public interface RoleService {
    RoleDto create(RoleDto roleDto);
    RoleDto readById(long id);
    RoleDto update(RoleDto roleDto, long id);
    void delete(long id);
    List<RoleDto> getAll();
}
