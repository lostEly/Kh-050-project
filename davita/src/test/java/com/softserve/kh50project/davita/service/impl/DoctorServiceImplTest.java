package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.mapper.DoctorMapper;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.repository.DoctorRepository;
import com.softserve.kh50project.davita.specification.DoctorSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    private static final long USER_ID = 5L;
    private static final String SPECIALIZATION = "Surgeon";

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @Spy
    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    public void readById() {
        Doctor doctor = new Doctor();
        doctor.setUserId(USER_ID);

        doReturn(Optional.of(doctor)).when(doctorRepository).findById(anyLong());

        DoctorDto expected = new DoctorDto();
        expected.setUserId(USER_ID);

        doReturn(expected).when(doctorMapper).mapTo(any(Doctor.class));

        DoctorDto actual = doctorService.readById(USER_ID);

        verify(doctorRepository).findById(USER_ID);
        verify(doctorMapper).mapTo(doctor);

        assertEquals(expected, actual);
    }

    @Test
    public void readByIdWithException() {
        doReturn(Optional.empty()).when(doctorRepository).findById(anyLong());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.readById(USER_ID));

        verify(doctorRepository).findById(USER_ID);
        verify(doctorMapper, never()).mapTo(any(Doctor.class));
    }

    @Test
    public void readAllWithSpecialization() {
        Doctor doctor1 = new Doctor();
        doctor1.setSpecialization(SPECIALIZATION);

        Doctor doctor2 = new Doctor();
        doctor2.setSpecialization(SPECIALIZATION);

        List<Doctor> doctors = List.of(doctor1, doctor2);

        doReturn(doctors).when(doctorRepository).findAll(any(DoctorSpecification.class));

        DoctorDto expected1 = new DoctorDto();
        expected1.setSpecialization(SPECIALIZATION);

        DoctorDto expected2 = new DoctorDto();
        expected2.setSpecialization(SPECIALIZATION);

        doReturn(expected1).doReturn(expected2).when(doctorMapper).mapTo(any(Doctor.class));

        List<DoctorDto> actual = doctorService.readAll(SPECIALIZATION);

        verify(doctorRepository).findAll(any(DoctorSpecification.class));
        verify(doctorMapper, times(2)).mapTo(any(Doctor.class));

        assertEquals(2, actual.size());
        assertTrue(actual.contains(expected1));
        assertTrue(actual.contains(expected2));
    }

    @Test
    public void readAllWithNullSpecialization() {
        Doctor doctor1 = new Doctor();
        doctor1.setSpecialization(SPECIALIZATION);

        Doctor doctor2 = new Doctor();
        doctor2.setSpecialization(SPECIALIZATION);

        List<Doctor> doctors = List.of(doctor1, doctor2);

        doReturn(doctors).when(doctorRepository).findAll(any(DoctorSpecification.class));

        DoctorDto expected1 = new DoctorDto();
        expected1.setSpecialization(SPECIALIZATION);

        DoctorDto expected2 = new DoctorDto();
        expected2.setSpecialization(SPECIALIZATION);

        doReturn(expected1).doReturn(expected2).when(doctorMapper).mapTo(any(Doctor.class));

        List<DoctorDto> actual = doctorService.readAll(null);

        verify(doctorRepository).findAll(any(DoctorSpecification.class));
        verify(doctorMapper, times(2)).mapTo(any(Doctor.class));

        assertEquals(2, actual.size());
        assertTrue(actual.contains(expected1));
        assertTrue(actual.contains(expected2));
    }

    @Test
    public void create() {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setSpecialization(SPECIALIZATION);

        Doctor doctor = new Doctor();
        doctor.setSpecialization(SPECIALIZATION);

        doReturn(doctor).when(doctorMapper).mapFrom(any(DoctorDto.class));

        doReturn(doctor).when(doctorRepository).save(any(Doctor.class));

        DoctorDto expected = new DoctorDto();
        expected.setSpecialization(SPECIALIZATION);

        doReturn(expected).when(doctorMapper).mapTo(any(Doctor.class));

        DoctorDto actual = doctorService.create(doctorDto);

        verify(doctorMapper).mapFrom(doctorDto);
        verify(doctorRepository).save(doctor);
        verify(doctorMapper).mapTo(doctor);

        assertEquals(expected, actual);
    }

    @Test
    public void update() {
        DoctorDto doctorDto = new DoctorDto();

        Doctor doctor = new Doctor();
        doctor.setUserId(USER_ID);

        doReturn(doctor).when(doctorMapper).mapFrom(any(DoctorDto.class));

        doReturn(doctor).when(doctorRepository).save(any(Doctor.class));

        DoctorDto expected = new DoctorDto();
        expected.setUserId(USER_ID);

        doReturn(expected).when(doctorMapper).mapTo(any(Doctor.class));

        DoctorDto actual = doctorService.update(doctorDto, USER_ID);

        verify(doctorMapper).mapFrom(doctorDto);
        verify(doctorRepository).save(doctor);
        verify(doctorMapper).mapTo(doctor);

        assertEquals(expected, actual);
    }

    @Test
    public void patch() {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setUserId(USER_ID);

        doReturn(doctorDto).when(doctorService).readById(anyLong());

        Doctor doctor = new Doctor();
        doctor.setUserId(USER_ID);
        doctor.setSpecialization(SPECIALIZATION);

        doReturn(doctor).when(doctorMapper).mapFrom(any(DoctorDto.class));

        doReturn(doctor).when(doctorRepository).save(any(Doctor.class));

        DoctorDto expected = new DoctorDto();
        expected.setUserId(USER_ID);
        expected.setSpecialization(SPECIALIZATION);

        doReturn(expected).when(doctorMapper).mapTo(any(Doctor.class));

        DoctorDto actual = doctorService.patch(Map.of("specialization", SPECIALIZATION), USER_ID);

        verify(doctorService).readById(USER_ID);
        verify(doctorMapper).mapFrom(doctorDto);
        verify(doctorRepository).save(doctor);
        verify(doctorMapper).mapTo(doctor);

        assertEquals(expected, actual);
    }

    @Test
    public void delete() {
        doctorService.delete(USER_ID);
        verify(doctorRepository).deleteById(USER_ID);
    }
}