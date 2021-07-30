package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.mapper.PatientMapper;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.repository.PatientRepository;
import com.softserve.kh50project.davita.specification.PatientSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {
    private static final long USER_ID = 5L;
    static final String JOHN = "John";
    static final String LAST_NAME = "Watson";
    static final LocalDate BIRTHDAY = LocalDate.of(1963, 10, 23);
    static final String INSURANCE_NUMBER = "1234567";

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Spy
    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void readById() {
        Patient patient = new Patient();
        patient.setUserId(USER_ID);

        doReturn(Optional.of(patient)).when(patientRepository).findById(anyLong());

        PatientDto expected = new PatientDto();
        expected.setUserId(USER_ID);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        PatientDto actual = patientService.readById(USER_ID);

        verify(patientRepository).findById(USER_ID);
        verify(patientMapper).mapTo(patient);

        assertEquals(expected, actual);
    }

    @Test
    void readByIdWithException() {
        doReturn(Optional.empty()).when(patientRepository).findById(anyLong());

        assertThrows(ResourceNotFoundException.class, () -> patientService.readById(USER_ID));

        verify(patientRepository).findById(USER_ID);
        verify(patientMapper, never()).mapTo(any(Patient.class));
    }

    @Test
    void readAllWithParameters() {
        Patient patient = new Patient();
        patient.setName(JOHN);
        patient.setLastName(LAST_NAME);
        patient.setDateOfBirthday(BIRTHDAY);

        List<Patient> patients = List.of(patient);

        doReturn(patients).when(patientRepository).findAll(any(PatientSpecification.class));

        PatientDto expected = new PatientDto();
        expected.setName(JOHN);
        expected.setLastName(LAST_NAME);
        expected.setDateOfBirthday(BIRTHDAY);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        List<PatientDto> actual = patientService.readAll(JOHN, LAST_NAME, BIRTHDAY);

        verify(patientRepository).findAll(any(PatientSpecification.class));
        verify(patientMapper).mapTo(any(Patient.class));

        assertEquals(1, actual.size());
        assertEquals(expected.getName(), actual.get(0).getName());
        assertEquals(expected.getLastName(), actual.get(0).getLastName());
        assertEquals(expected.getDateOfBirthday(), actual.get(0).getDateOfBirthday());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void readAllWithNullParameters() {
        Patient patient = new Patient();
        patient.setName(JOHN);
        patient.setLastName(LAST_NAME);
        patient.setDateOfBirthday(BIRTHDAY);

        List<Patient> patients = List.of(patient);

        doReturn(patients).when(patientRepository).findAll(any(PatientSpecification.class));

        PatientDto expected = new PatientDto();
        expected.setName(JOHN);
        expected.setLastName(LAST_NAME);
        expected.setDateOfBirthday(BIRTHDAY);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        List<PatientDto> actual = patientService.readAll(null, null, null);

        verify(patientRepository).findAll(any(PatientSpecification.class));
        verify(patientMapper).mapTo(any(Patient.class));


        verify(patientRepository).findAll(any(PatientSpecification.class));
        verify(patientMapper).mapTo(any(Patient.class));

        assertEquals(1, actual.size());
        assertTrue(actual.contains(expected));
    }

    @Test
    void create() {
        PatientDto patientDto = new PatientDto();
        patientDto.setInsuranceNumber(INSURANCE_NUMBER);

        Patient patient = new Patient();
        patient.setInsuranceNumber(INSURANCE_NUMBER);

        doReturn(patient).when(patientMapper).mapFrom(any(PatientDto.class));

        doReturn(patient).when(patientRepository).save(any(Patient.class));

        PatientDto expected = new PatientDto();
        expected.setInsuranceNumber(INSURANCE_NUMBER);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        PatientDto actual = patientService.create(patientDto);

        verify(patientMapper).mapFrom(patientDto);
        verify(patientRepository).save(patient);
        verify(patientMapper).mapTo(patient);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        PatientDto patientDto = new PatientDto();
        patientDto.setUserId(USER_ID);

        doReturn(patientDto).when(patientService).readById(anyLong());

        Patient patient = new Patient();
        patient.setUserId(USER_ID);

        doReturn(patient).when(patientMapper).mapFrom(any(PatientDto.class));

        doReturn(patient).when(patientRepository).save(any(Patient.class));

        PatientDto expected = new PatientDto();
        expected.setUserId(USER_ID);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        PatientDto actual = patientService.update(patientDto, USER_ID);

        verify(patientService).readById(USER_ID);
        verify(patientMapper).mapFrom(patientDto);
        verify(patientRepository).save(patient);
        verify(patientMapper).mapTo(patient);

        assertEquals(expected, actual);
    }

    @Test
    void patch() {
        PatientDto patientDto = new PatientDto();
        patientDto.setUserId(USER_ID);

        doReturn(patientDto).when(patientService).readById(anyLong());

        Patient patient = new Patient();
        patient.setUserId(USER_ID);
        patient.setInsuranceNumber(INSURANCE_NUMBER);

        doReturn(patient).when(patientMapper).mapFrom(any(PatientDto.class));

        doReturn(patient).when(patientRepository).save(any(Patient.class));

        PatientDto expected = new PatientDto();
        expected.setUserId(USER_ID);
        expected.setInsuranceNumber(INSURANCE_NUMBER);

        doReturn(expected).when(patientMapper).mapTo(any(Patient.class));

        PatientDto actual = patientService.patch(Map.of("insuranceNumber", INSURANCE_NUMBER), USER_ID);

        verify(patientService).readById(USER_ID);
        verify(patientMapper).mapFrom(patientDto);
        verify(patientRepository).save(patient);
        verify(patientMapper).mapTo(patient);

        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        patientService.delete(USER_ID);
        verify(patientRepository).deleteById(USER_ID);
    }

}