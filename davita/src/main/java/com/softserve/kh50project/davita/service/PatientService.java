package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.PatientDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PatientService {
    PatientDto readById(Long id);

    List<PatientDto> readAll(String name, String lastName, LocalDate dateOfBirth);

    PatientDto create(PatientDto patientDto);

    PatientDto update(PatientDto patientDto, Long id);

    PatientDto patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
