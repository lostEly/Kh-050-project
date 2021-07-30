package com.softserve.kh50project.davita.service;

import com.softserve.kh50project.davita.dto.DoctorDto;

import java.util.List;
import java.util.Map;

public interface DoctorService {

    DoctorDto readById(Long id);

    List<DoctorDto> readAll(String specialization);

    DoctorDto create(DoctorDto doctorDto);

    DoctorDto update(DoctorDto doctorDto, Long id);

    DoctorDto patch(Map<String, Object> fields, Long id);

    void delete(Long id);
}
