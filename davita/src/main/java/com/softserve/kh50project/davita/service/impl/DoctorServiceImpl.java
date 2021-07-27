package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.repository.DoctorRepository;
import com.softserve.kh50project.davita.service.DoctorService;
import com.softserve.kh50project.davita.specification.DoctorSpecification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor readById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Doctor> readAll(String specialization) {
        return doctorRepository.findAll(DoctorSpecification.create(specialization));
    }

    @Override
    public Doctor create(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor update(Doctor doctor, Long id) {
        doctor.setUserId(id);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor patch(Map<String, Object> fields, Long id) {
        Doctor doctor = readById(id);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Doctor.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, doctor, v);
        });
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
}
