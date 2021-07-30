package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Equipment;

import java.util.List;
import java.util.Map;

public interface EquipmentService {
    Equipment readById(Long id);

    List<Equipment> read();

    Equipment create(Equipment equipment);

    Equipment update(Equipment equipment, Long id);

    Equipment patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
