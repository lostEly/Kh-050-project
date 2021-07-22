package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    // get doctor by id
    @GetMapping(value = "/{id}")
    public Doctor readById(@PathVariable Long id) {
        Doctor doctor = new Doctor();
        doctor.setUserId(id);
        return doctor;
    }

    // get list of all doctors or doctors with some specialization
    @GetMapping
    public List<Doctor> read(@RequestParam(value = "specialization", required = false) String specialization) {
        Doctor doctor = new Doctor();
        Doctor doctor1 = new Doctor();
        Doctor doctor2 = new Doctor();
        if (Objects.isNull(specialization)) {
            return new ArrayList<>(Arrays.asList(doctor, doctor1, doctor2));
        }
        return new ArrayList<>(Arrays.asList(doctor, doctor2));

    }

    // create doctor
    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor doctor) {
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    // update doctor
    @PutMapping(value = "/{id}")
    public ResponseEntity<Doctor> update(@RequestBody Doctor doctor, @PathVariable Long id) {
        doctor.setName("John");
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    /**
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated doctor
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Doctor> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        return new ResponseEntity<>(new Doctor(), HttpStatus.OK);
    }

    // delete doctor
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
    }
}
