package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.mapper.PatientMapper;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.repository.PatientRepository;
import com.softserve.kh50project.davita.service.PatientService;
import com.softserve.kh50project.davita.specification.PatientSpecification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientDto readById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::mapTo)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " "));
    }

    @Override
    public List<PatientDto> readAll(String name, String lastName, LocalDate dateOfBirth) {
        return patientRepository.findAll(PatientSpecification.create(name, lastName, dateOfBirth)).stream()
                .map(patientMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDto create(PatientDto patientDto) {
        Patient patient = patientMapper.mapFrom(patientDto);
        return patientMapper.mapTo(patientRepository.save(patient));
    }

    @Override
    public PatientDto update(PatientDto patientDto, Long id) {
        PatientDto existingPatient = readById(id);
        patientDto.setUserId(existingPatient.getUserId());
        Patient patient = patientMapper.mapFrom(patientDto);
        return patientMapper.mapTo(patientRepository.save(patient));
    }

    @Override
    public PatientDto patch(Map<String, Object> fields, Long id) {
        PatientDto patientDto = readById(id);
        fields.forEach((k, v) -> {
            if (Objects.equals(k, "dateOfBirthday")) {
                v = LocalDate.parse((String) v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            Field field = ReflectionUtils.findField(PatientDto.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, patientDto, v);
        });

        Patient patient = patientMapper.mapFrom(patientDto);
        return patientMapper.mapTo(patientRepository.save(patient));
    }

    @Override
    public void delete(Long id) {
        try {
            readById(id);
        } catch (ResourceNotFoundException e) {
            return;
        }

        patientRepository.deleteById(id);
    }
}
