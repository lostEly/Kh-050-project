package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/procedures")
@AllArgsConstructor
public class ProcedureController {

    @Qualifier(value = "ProcedureServiceImpl")
    private final ProcedureService procedureService;

    /**
     * Getting all procedures by parameters
     *
     * @param name     optional, procedure name
     * @param cost     optional, procedure cost
     * @param duration optional, procedure duration
     * @return the list of procedures, return empty list if the procedure wasn't found
     */
    @GetMapping
    public ResponseEntity<List<Procedure>> read(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "cost", required = false) Double cost,
                                                @RequestParam(value = "duration", required = false) String duration) {
        List<Procedure> procedures = procedureService.read(name, cost, duration);
        return new ResponseEntity<>(procedures, HttpStatus.OK);
    }

    /**
     * Getting the procedure by id
     *
     * @param id procedure id
     * @return the procedure by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Procedure> readById(@PathVariable Long id) {
        Procedure procedure = procedureService.readById(id);
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }

    /**
     * Creating new procedure
     *
     * @param procedure which should be created
     * @return the created procedure
     */
    @PostMapping
    public ResponseEntity<Procedure> create(@RequestBody Procedure procedure) {
        Procedure createdProcedure = procedureService.create(procedure);
        return new ResponseEntity<>(createdProcedure, HttpStatus.CREATED);
    }

    /**
     * Updating the procedure
     *
     * @param procedure to be updated
     * @param id        of the procedure
     * @return updated procedure
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Procedure> update(@RequestBody Procedure procedure, @PathVariable Long id) {
        Procedure updatedProcedure = procedureService.update(procedure, id);
        return new ResponseEntity<>(updatedProcedure, HttpStatus.OK);
    }

    /**
     * Partial updating of the procedure
     *
     * @param fields is the map of fields to be updated and new values
     * @param id     of the procedure
     * @return updated procedure
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Procedure> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Procedure patchedProcedure = procedureService.patch(fields, id);
        return new ResponseEntity<>(patchedProcedure, HttpStatus.OK);
    }

    /**
     * Registering equipment to the procedure
     *
     * @param procedureId of procedure to register equipment
     * @param equipmentId of equipment to register
     */
    @PatchMapping(value = "/{procedureId}/register-equipment/{equipmentId}")
    public void registerEquipment(@PathVariable Long procedureId,
                                  @PathVariable Long equipmentId) {
        procedureService.registerEquipment(procedureId, equipmentId);
    }

    /**
     * Deleting the procedure by id
     *
     * @param id of the procedure
     * @return status code 200 OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Procedure> delete(@PathVariable Long id) {
        procedureService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}