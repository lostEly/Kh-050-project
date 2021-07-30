package com.softserve.kh50project.davita.mapper;

import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper extends AbstractMapper<Patient, PatientDto> {

    @Override
    public PatientDto mapTo(Patient input) {
        PatientDto patientDto = new PatientDto();
        patientDto.setUserId(input.getUserId());
        patientDto.setName(input.getName());
        patientDto.setLastName(input.getLastName());
        patientDto.setLogin(input.getLogin());
        patientDto.setPassword(input.getPassword());
        patientDto.setPhone(input.getPhone());
        patientDto.setDateOfBirthday(input.getDateOfBirthday());
        patientDto.setEmail(input.getEmail());
        patientDto.setInsuranceNumber(input.getInsuranceNumber());

        return patientDto;
    }

    @Override
    public Patient mapFrom(PatientDto input) {
        Patient patient = new Patient();
        patient.setUserId(input.getUserId());
        patient.setName(input.getName());
        patient.setLastName(input.getLastName());
        patient.setLogin(input.getLogin());
        patient.setPassword(input.getPassword());
        patient.setPhone(input.getPhone());
        patient.setDateOfBirthday(input.getDateOfBirthday());
        patient.setEmail(input.getEmail());
        patient.setInsuranceNumber(input.getInsuranceNumber());

        return patient;
    }
}
