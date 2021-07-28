package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.repository.ProcedureRepository;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProcedureServiceImpl implements ProcedureService {
    private final ProcedureRepository procedureRepository;

    @Override
    public Procedure readById(Long id) {
        return procedureRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Procedure> read(String name, Double cost, String duration) {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery(name, cost, duration);
        return procedureRepository.findAll(specification);
    }

    @Override
    public Procedure create(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @Override
    public Procedure update(Procedure newProcedure, Long id) {
        newProcedure.setProcedureId(id);
        return procedureRepository.save(newProcedure);
    }

    @Override
    public Procedure patch(Map<String, Object> fields, Long id) {
        Procedure procedure = readById(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Procedure.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, procedure, v);
        });
        return procedureRepository.save(procedure);
    }

    @Override
    public void delete(Long id) {
        procedureRepository.deleteById(id);
    }

    @Override
    public void registerEquipment(Long procedureId, Long equipmentId) {
        procedureRepository.registerEquipment(procedureId, equipmentId);
    } 
}
