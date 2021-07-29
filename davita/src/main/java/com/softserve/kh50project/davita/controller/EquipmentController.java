package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipment")
@AllArgsConstructor
public class EquipmentController {
    @Qualifier(value = "EquipmentServiceImpl")
    private final EquipmentService equipmentService;

    /**
     * @return the list of equipment, return empty list if the equipment wasn't found
     */
    @GetMapping
    public ResponseEntity<List<Equipment>> read() {
        List<Equipment> equipmentList = equipmentService.read();
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);
    }

    /**
     * Getting the equipment by id
     *
     * @param id equipment id
     * @return the equipment by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Equipment> readById(@PathVariable Long id) {
        Equipment equipment = equipmentService.readById(id);
        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    /**
     * Creating new equipment
     *
     * @param equipment which should be created
     * @return the created equipment
     */
    @PostMapping("")
    public ResponseEntity<Equipment> create(@RequestBody Equipment equipment) {
        Equipment createdEquipment= equipmentService.create(equipment);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    /**
     * Updating the equipment
     *
     * @param equipment to be updated
     * @param id        of the equipment
     * @return updated equipment
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Equipment> update(@RequestBody Equipment equipment, @PathVariable Long id) {
        Equipment equipmentU = equipmentService.update(equipment, id);
        return new ResponseEntity<>(equipmentU, HttpStatus.OK);
    }

    /**
     * Partial updating of the equipment
     *
     * @param fields is the map of fields to be updated and new values
     * @param id     of the equipment
     * @return updated equipment
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Equipment> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Equipment equipment = equipmentService.patch(fields, id);
        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    /**
     * Deleting the equipment by id
     *
     * @param id of the equipment
     * @return status code 200 OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Equipment> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}