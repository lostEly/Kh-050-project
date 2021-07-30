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

    /**
     * Getting patient by id
     *
     * @param id patient id
     * @return the patient by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Patient> readById(@PathVariable Long id) {
        Patient patient = new Patient();
        patient.setUserId(id);

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Getting all patients or patient by parameters
     *
     * @param name optional, patient name
     * @param lastName optional, patient lastname
     * @param dateOfBirth optional, patient date of birth
     * @return the list of patients, return empty list if the patient wasn't found
     */
    @GetMapping
    public ResponseEntity<List<Patient>> read(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "lastName", required = false) String lastName,
                              @RequestParam(value = "dateOfBirth", required = false) LocalDate dateOfBirth) {
        Patient patient = new Patient();
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();

        if (Objects.isNull(name) && Objects.isNull(lastName) && Objects.isNull(dateOfBirth)) {
            return new ResponseEntity<>(List.of(patient, patient1, patient2), HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of(patient), HttpStatus.OK);

    }

    /**
     * Creating a patient
     *
     * @param patient which should be create
     * @return the created patient
     */
    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    /**
     * Updating the patient
     *
     * @param patient which should be update
     * @param id
     * @return the updated patient
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Patient> update(@RequestBody Patient patient, @PathVariable Long id) {
        patient.setName("John");
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Partial updating the patient
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated patient
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Patient> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        return new ResponseEntity<>(new Patient(), HttpStatus.OK);
    }

    /**
     * Deleting the patient
     *
     * @param id patient id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
