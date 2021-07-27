package com.softserve.kh50project.davita.service.impl;


import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.repository.PatientRepository;
import com.softserve.kh50project.davita.service.PatientService;
import com.softserve.kh50project.davita.specification.PatientSpecification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient readById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Patient> readAll(String name, String lastName, LocalDate dateOfBirth) {
        return patientRepository.findAll(PatientSpecification.create(name, lastName, dateOfBirth));
    }

    @Override
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient, Long id) {
        patient.setUserId(id);
        return patientRepository.save(patient);
    }

    @Override
    public Patient patch(Map<String, Object> fields, Long id) {
        Patient patient = readById(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Patient.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, patient, v);
        });
        return patientRepository.save(patient);
    }

    @Override
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}
