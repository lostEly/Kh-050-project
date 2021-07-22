package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Doctor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    /**
     * Getting doctor by id
     *
     * @param id doctor id
     * @return the doctor by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Doctor> readById(@PathVariable Long id) {
        Doctor doctor = new Doctor();
        doctor.setUserId(id);

        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    /**
     * Getting all doctors or doctors with some specialization
     *
     * @param specialization optional, doctor specialization
     * @return the list of doctors
     */
    @GetMapping
    public ResponseEntity<List<Doctor>> read(@RequestParam(value = "specialization", required = false) String specialization) {
        Doctor doctor = new Doctor();
        Doctor doctor1 = new Doctor();
        Doctor doctor2 = new Doctor();

        if (Objects.isNull(specialization)) {
            return new ResponseEntity<>(List.of(doctor, doctor1, doctor2), HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of(doctor, doctor2), HttpStatus.OK);

    }

    /**
     * Creating a doctor
     *
     * @param doctor which should be create
     * @return the created doctor
     */
    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor doctor) {
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    /**
     * Updating the doctor
     *
     * @param doctor which should be update
     * @param id
     * @return the updated doctor
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Doctor> update(@RequestBody Doctor doctor, @PathVariable Long id) {
        doctor.setName("John");
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    /**
     * Partial updating the doctor
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated doctor
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Doctor> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        return new ResponseEntity<>(new Doctor(), HttpStatus.OK);
    }

    /**
     * Deleting the doctor
     *
     * @param id doctor id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
