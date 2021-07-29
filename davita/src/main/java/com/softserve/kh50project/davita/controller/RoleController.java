package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Role;
import com.softserve.kh50project.davita.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.readById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.create(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Role update(@RequestBody Role role, @PathVariable Long id){
        return roleService.update(role, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        roleService.delete(id);
    }
}
