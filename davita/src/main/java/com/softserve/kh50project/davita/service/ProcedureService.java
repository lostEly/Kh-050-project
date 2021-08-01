package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.model.Procedure;

import java.util.List;
import java.util.Map;

public interface ProcedureService {
    ProcedureDto readById(Long id);

    List<ProcedureDto> read(String name, Double cost, String duration);

    ProcedureDto create(ProcedureDto procedureDto);

    ProcedureDto update(ProcedureDto procedureDto, Long id);

    ProcedureDto patch(Map<String, Object> fields, Long id);

    void delete(Long id);

    void registerEquipment(Long procedureId, Long equipmentId);
}
