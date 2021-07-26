package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.repository.EquipmentRepository;
import lombok.AllArgsConstructor;



import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;


    @Override
    public Equipment readById(Long id) {
        return equipmentRepository
                .findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Equipment> read() {
        return equipmentRepository.findAll();
    }

    @Override
    public Equipment create(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment update(Equipment equipment, Long id) {
        equipment.setEquipmentId(id);
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment patch(Map<String, Object> fields, Long id) {
        Equipment equipment = readById(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Procedure.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, equipment, v);
        });
        return equipmentRepository.save(equipment);
    }

    @Override
    public void delete(Long id) {
        equipmentRepository.deleteById(id);
    }

}