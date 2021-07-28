package com.softserve.kh50project.davita.mapper;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper extends AbstractMapper<Doctor, DoctorDto> {

    @Override
    public DoctorDto mapTo(Doctor input) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setUserId(input.getUserId());
        doctorDto.setName(input.getName());
        doctorDto.setLastName(input.getLastName());
        doctorDto.setLogin(input.getLogin());
        doctorDto.setPassword(input.getPassword());
        doctorDto.setPhone(input.getPhone());
        doctorDto.setDateOfBirthday(input.getDateOfBirthday());
        doctorDto.setEmail(input.getEmail());
        doctorDto.setCertificateNumber(input.getCertificateNumber());
        doctorDto.setSpecialization(input.getSpecialization());

        return doctorDto;
    }

    @Override
    public Doctor mapFrom(DoctorDto input) {
        Doctor doctor = new Doctor();
        doctor.setUserId(input.getUserId());
        doctor.setName(input.getName());
        doctor.setLastName(input.getLastName());
        doctor.setLogin(input.getLogin());
        doctor.setPassword(input.getPassword());
        doctor.setPhone(input.getPhone());
        doctor.setDateOfBirthday(input.getDateOfBirthday());
        doctor.setEmail(input.getEmail());
        doctor.setCertificateNumber(input.getCertificateNumber());
        doctor.setSpecialization(input.getSpecialization());

        return doctor;
    }
}
