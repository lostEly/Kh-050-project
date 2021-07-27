package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PatientService {
    Patient readById(Long id);

    List<Patient> readAll(String name, String lastName, LocalDate dateOfBirth);

    Patient create(Patient patient);

    Patient update(Patient patient, Long id);

    Patient patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
