package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Getting doctor by id
     *
     * @param id doctor id
     * @return the doctor by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<DoctorDto> readById(@PathVariable Long id) {
        DoctorDto doctor = doctorService.readById(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    /**
     * Getting all doctors or doctors with some specialization
     *
     * @param specialization optional, doctor specialization
     * @return the list of doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorDto>> readAll(
            @RequestParam(value = "specialization", required = false) String specialization) {

        List<DoctorDto> doctors = doctorService.readAll(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    /**
     * Creating a doctorDto
     *
     * @param doctorDto which should be create
     * @return the created doctorDto
     */
    @PostMapping
    public ResponseEntity<DoctorDto> create(@RequestBody DoctorDto doctorDto) {
        DoctorDto createdDoctor = doctorService.create(doctorDto);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    /**
     * Updating the doctorDto
     *
     * @param doctorDto which should be update
     * @param id
     * @return the updated doctorDto
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<DoctorDto> update(@RequestBody DoctorDto doctorDto, @PathVariable Long id) {
        DoctorDto updatedDoctor = doctorService.update(doctorDto, id);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    /**
     * Partial updating the doctor
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated doctor
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<DoctorDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        DoctorDto patchedDoctor = doctorService.patch(fields, id);
        return new ResponseEntity<>(patchedDoctor, HttpStatus.OK);
    }

    /**
     * Deleting the doctor
     *
     * @param id doctor id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
