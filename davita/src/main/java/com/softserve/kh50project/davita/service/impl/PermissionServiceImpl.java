package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.model.Permission;
import com.softserve.kh50project.davita.repository.PermissionRepository;
import com.softserve.kh50project.davita.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;


    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission readById(long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(("Permission with id = " + id + " does not exist!")));
    }

    @Override
    public Permission update(Permission newPermission, long id) {
        newPermission.setPermissionId(id);
        return permissionRepository.save(newPermission);
    }

    @Override
    public void delete(long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }
}
