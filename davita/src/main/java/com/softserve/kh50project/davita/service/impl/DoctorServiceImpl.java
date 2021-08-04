package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.mapper.DoctorMapper;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.repository.DoctorRepository;
import com.softserve.kh50project.davita.service.DoctorService;
import com.softserve.kh50project.davita.specification.DoctorSpecification;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorDto readById(Long id) {
        return doctorRepository.findById(id)
                .map(doctorMapper::mapTo)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " "));
    }

    @Override
    public List<DoctorDto> readAll(String specialization) {
        return doctorRepository.findAll(DoctorSpecification.create(specialization)).stream()
                .map(doctorMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDto create(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.mapFrom(doctorDto);
        return doctorMapper.mapTo(doctorRepository.save(doctor));
    }

    @Override
    public DoctorDto update(DoctorDto doctorDto, Long id) {
        DoctorDto existingDoctor = readById(id);
        doctorDto.setUserId(existingDoctor.getUserId());
        Doctor doctor = doctorMapper.mapFrom(doctorDto);
        return doctorMapper.mapTo(doctorRepository.save(doctor));
    }

    @Override
    public DoctorDto patch(Map<String, Object> fields, Long id) {
        DoctorDto doctorDto = readById(id);
        fields.forEach((k, v) -> {
            if (Objects.equals(k, "dateOfBirthday")) {
                v = LocalDate.parse((String) v, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            Field field = ReflectionUtils.findField(DoctorDto.class, k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, doctorDto, v);
        });

        Doctor doctor = doctorMapper.mapFrom(doctorDto);
        return doctorMapper.mapTo(doctorRepository.save(doctor));
    }

    @Override
    public void delete(Long id) {
        try {
            readById(id);
        } catch (ResourceNotFoundException e) {
            return;
        }

        doctorRepository.deleteById(id);
    }
}
