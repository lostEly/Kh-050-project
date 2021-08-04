package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.EquipmentDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.model.Equipment;

import java.util.List;
import java.util.Map;

public interface EquipmentService {
    EquipmentDto readById(Long id);

    List<EquipmentDto> read(String name);

    EquipmentDto create(EquipmentDto equipment);

    EquipmentDto update(EquipmentDto equipment, Long id);

    EquipmentDto patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
