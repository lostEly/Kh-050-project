package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Getting patient by id
     *
     * @param id patient id
     * @return the patient by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Patient> readById(@PathVariable Long id) {
        Patient patient = patientService.readById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Getting all patients or patient by parameters
     *
     * @param name        optional, patient name
     * @param lastName    optional, patient lastname
     * @param dateOfBirth optional, patient date of birth
     * @return the list of patients, return empty list if the patient wasn't found
     */
    @GetMapping
    public ResponseEntity<List<Patient>> readAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "dateOfBirth", required = false) LocalDate dateOfBirth) {
        List<Patient> patients = patientService.readAll(name, lastName, dateOfBirth);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    /**
     * Creating a patient
     *
     * @param patient which should be create
     * @return the created patient
     */
    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody Patient patient) {
        Patient createdPatient = patientService.create(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
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
        Patient updatedPatient = patientService.update(patient, id);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    /**
     * Partial updating the patient
     *
     * @param fields is map where keys are updated fields and values are field values
     * @return partly updated patient
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Patient> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Patient patchedPatient = patientService.patch(fields, id);
        return new ResponseEntity<>(patchedPatient, HttpStatus.OK);
    }

    /**
     * Deleting the patient
     *
     * @param id patient id
     * @return ResponseEntity with status OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        patientService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
