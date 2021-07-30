package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Permission;
import com.softserve.kh50project.davita.model.Role;
import com.softserve.kh50project.davita.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/permissions")
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Permission> getAll() {
        return permissionService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Long id) {
        return permissionService.readById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission create(@RequestBody Permission permission) {
        return permissionService.create(permission);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Permission update(@RequestBody Permission permission, @PathVariable Long id){
        return permissionService.update(permission, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        permissionService.delete(id);
    }
}
