package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.repository.ProcedureRepository;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProcedureServiceImpl implements ProcedureService {
    private final ProcedureRepository procedureRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProcedureDto readById(Long id) {
        Procedure procedure = procedureRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return convertProcedureToDto(procedure);
    }

    @Override
    public List<ProcedureDto> read(String name, Double cost, String duration) {
        Specification<Procedure> specification = procedureRepository.getProcedureQuery(name, cost, duration);
        List<Procedure> procedures = procedureRepository.findAll(specification);
        return procedures
                .stream()
                .map(this::convertProcedureToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProcedureDto create(ProcedureDto procedureDto) {
        Procedure createdProcedure = convertDtoToProcedure(procedureDto);
        procedureRepository.save(createdProcedure);
        return convertProcedureToDto(createdProcedure);
    }

    @Override
    public ProcedureDto update(ProcedureDto newProcedure, Long id) {
        newProcedure.setProcedureId(id);
        Procedure procedure = convertDtoToProcedure(newProcedure);
        procedureRepository.save(procedure);
        return convertProcedureToDto(procedure);
    }

    @Override
    public ProcedureDto patch(Map<String, Object> fields, Long id) {
        Procedure procedure = procedureRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Procedure.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, procedure, v);
        });
        Procedure patchedProcedure = procedureRepository.save(procedure);
        return convertProcedureToDto(patchedProcedure);
    }

    @Override
    public void delete(Long id) {
        procedureRepository.deleteById(id);
    }

    @Override
    public void registerEquipment(Long procedureId, Long equipmentId) {
        procedureRepository.registerEquipment(procedureId, equipmentId);
    }

    /**
     * Converting procedure to DTO
     *
     * @param procedure to be converted
     * @return ProcedureDTO object
     */
    private ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.convertLocalTimeToString(procedure.getDuration());
        return procedureDto;
    }

    /**
     * Converting ProcedureDto to Procedure
     *
     * @param procedureDto to be converted
     * @return Procedure object
     */
    private Procedure convertDtoToProcedure(ProcedureDto procedureDto) {
        Procedure procedure = modelMapper.map(procedureDto, Procedure.class);
        procedure.setDuration(procedureDto.convertStringToLocalTime());
        return procedure;
    }

}