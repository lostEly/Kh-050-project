package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.EquipmentDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;
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

/*    *//**
     * @return the list of equipment, return empty list if the equipment wasn't found
     *//*
    @GetMapping
    public ResponseEntity<List<Equipment>> read() {
        List<Equipment> equipmentList = equipmentService.read();
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);
    }*/

    /**
     * Getting the equipment by id
     *
     * @param id equipment id
     * @return the equipment by id
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EquipmentDto> readById(@PathVariable Long id) {
        EquipmentDto equipmentDto = equipmentService.readById(id);
        return new ResponseEntity<>(equipmentDto, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<EquipmentDto>> read(@RequestParam(value = "name", required = false) String name) {
        List<EquipmentDto> equipmentList = equipmentService.read(name);
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);
    }

    /**
     * Creating new equipment
     *
     * @param equipmentDto which should be created
     * @return the created equipment
     */
    @PostMapping("")
    public ResponseEntity<EquipmentDto> create(@RequestBody EquipmentDto equipmentDto) {
        EquipmentDto createdEquipment= equipmentService.create(equipmentDto);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    /**
     * Updating the equipment
     *
     * @param equipmentDto to be updated
     * @param id        of the equipment
     * @return updated equipment
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EquipmentDto> update(@RequestBody EquipmentDto equipmentDto, @PathVariable Long id) {
        EquipmentDto updatedEquipmentDto = equipmentService.update(equipmentDto, id);
        return new ResponseEntity<>(updatedEquipmentDto, HttpStatus.OK);
    }

    /**
     * Partial updating of the equipment
     *
     * @param fields is the map of fields to be updated and new values
     * @param id     of the equipment
     * @return updated equipment
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<EquipmentDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        EquipmentDto patchedEquipmentDto = equipmentService.patch(fields, id);
        return new ResponseEntity<>(patchedEquipmentDto, HttpStatus.OK);
    }

    /**
     * Deleting the equipment by id
     *
     * @param id of the equipment
     * @return status code 200 OK
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
