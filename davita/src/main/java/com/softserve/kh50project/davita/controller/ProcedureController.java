package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    /**
     * Getting all procedures by parameters
     *
     * @param name optional, procedure name
     * @param cost optional, procedure cost
     * @param duration optional, procedure duration
     * @return the list of procedures, return empty list if the procedure wasn't found
     */
    @GetMapping
    public ResponseEntity<List<Procedure>> read(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "cost", required = false) Double cost,
                                                @RequestParam(value = "duration", required = false) LocalTime duration) {
        Procedure procedure1 = new Procedure();
        Procedure procedure2 = new Procedure();
        if (Objects.isNull(name) && Objects.isNull(cost) && Objects.isNull(duration)) {
            return new ResponseEntity<>(List.of(procedure1, procedure2), HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of(procedure1), HttpStatus.OK);
    }

    /**
     * Getting the procedure by id
     *
     * @param id procedure id
     * @return the procedure by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Procedure> readById(@PathVariable Long id) {
        Procedure procedure = new Procedure();
        procedure.setProcedureId(id);
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }

    /**
     * Creating new procedure
     *
     * @param procedure which should be created
     * @return the created procedure
     */
    @PostMapping("/add-procedure")
    public ResponseEntity<Procedure> create(@RequestBody Procedure procedure) {
        return new ResponseEntity<>(procedure, HttpStatus.CREATED);
    }

    /**
     * Updating the procedure
     *
     * @param procedure to be updated
     * @param id of the procedure
     * @return updated procedure
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Procedure> update(@RequestBody Procedure procedure, @PathVariable Long id) {
        procedure.setName("New name");
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }

    /**
     * Partial updating of the procedure
     *
     * @param fields is the map of fields to be updated and new values
     * @param id of the procedure
     * @return updated procedure
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Procedure> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        return new ResponseEntity<>(new Procedure(), HttpStatus.OK);
    }

    /**
     * Deleting the procedure by id
     *
     * @param id of the procedure
     * @return status code 200 OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Procedure> delete(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}