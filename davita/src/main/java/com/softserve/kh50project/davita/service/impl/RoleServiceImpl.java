package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.model.Role;
import com.softserve.kh50project.davita.repository.RoleRepository;
import com.softserve.kh50project.davita.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    //TODO change NoSuchElementException to EntityNotFoundException
    @Override
    public Role readById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(("Role with id=" + id + " does not exist!")));
    }

    @Override
    public Role update(Role newRole, long id) {
        newRole.setRoleId(id);
        return roleRepository.save(newRole);
    }

    @Override
    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
