package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.EquipmentDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.repository.EquipmentRepository;
import com.softserve.kh50project.davita.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public EquipmentDto readById(Long id) {
        Equipment equipment = equipmentRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return convertEquipmentToDto(equipment);
    }


    @Override
    public List<EquipmentDto> read(String name) {
        Specification<Equipment> specification = equipmentRepository.getEquipmentQuery(name);
        List<Equipment> equipmentList = equipmentRepository.findAll(specification);
        return equipmentList
                .stream()
                .map(this::convertEquipmentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentDto create(EquipmentDto equipmentDto) {
        Equipment createdEquipment = convertDtoToEquipment(equipmentDto);
        equipmentRepository.save(createdEquipment);
        return convertEquipmentToDto(createdEquipment);
    }

    @Override
    public EquipmentDto update(EquipmentDto newEquipment, Long id) {
        newEquipment.setEquipmentId(id);
        Equipment equipment = convertDtoToEquipment(newEquipment);
        equipmentRepository.save(equipment);
        return convertEquipmentToDto(equipment);
    }

    @Override
    public EquipmentDto patch(Map<String, Object> fields, Long id) {
        Equipment equipment = equipmentRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Equipment.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, equipment, v);
        });
        Equipment patchedEquipment = equipmentRepository.save(equipment);
        return convertEquipmentToDto(patchedEquipment);
    }


    /**
     * Converting procedure to DTO
     *
     * @param equipment to be converted
     * @return EquipmentDto object
     */
    public EquipmentDto convertEquipmentToDto(Equipment equipment) {
        EquipmentDto equipmentDto = modelMapper.map(equipment, EquipmentDto.class);
        return equipmentDto;
    }

    /**
     * Converting ProcedureDto to Procedure
     *
     * @param equipmentDto to be converted
     * @return Equipment object
     */
    public Equipment convertDtoToEquipment(EquipmentDto equipmentDto) {
        Equipment equipment = modelMapper.map(equipmentDto, Equipment.class);
        return equipment;
    }

    @Override
    public void delete(Long id) {
        try {
            readById(id);
        } catch (ResourceNotFoundException e) {
            return;
        }

        equipmentRepository.deleteById(id);
    }
}

