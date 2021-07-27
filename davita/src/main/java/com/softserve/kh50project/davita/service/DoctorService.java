package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.model.Doctor;

import java.util.List;
import java.util.Map;

public interface DoctorService {

    Doctor readById(Long id);

    List<Doctor> readAll(String specialization);

    Doctor create(Doctor doctor);

    Doctor update(Doctor doctor, Long id);

    Doctor patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
