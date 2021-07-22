package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    // get patient by id
    @GetMapping(value = "/{id}")
    public Patient readById(@PathVariable Long id) {
        Patient patient = new Patient();
        patient.setUserId(id);
        return patient;
    }

    // get list of all patients or patients by name and birthday
    @GetMapping
    public List<Patient> read(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "lastName", required = false) String lastName,
                              @RequestParam(value = "dateOfBirth", required = false) LocalDate dateOfBirth) {
        Patient patient = new Patient();
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        if (Objects.isNull(name) && Objects.isNull(lastName) && Objects.isNull(dateOfBirth)) {
            return List.of(patient, patient1, patient2);
        }
        return List.of(patient);

    }

    // create patient
    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    // update patient
    @PutMapping(value = "/{id}")
    public ResponseEntity<Patient> update(@RequestBody Patient patient, @PathVariable Long id) {
        patient.setName("John");
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Generation of lab numbers for standalone test
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated patient
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Patient> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        return new ResponseEntity<>(new Patient(), HttpStatus.OK);
    }

    // delete patient
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
    }
}
