package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.service.ProcedureService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

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
    public ResponseEntity<ProcedureDto> readById(@PathVariable Long id) {
        Procedure procedure = procedureService.readById(id);
        ProcedureDto procedureDto = convertProcedureToDto(procedure);
        return new ResponseEntity<>(procedureDto, HttpStatus.OK);
    }

    /**
     * Creating new procedure
     *
     * @param procedureDto which should be created
     * @return the created procedure
     */
    @PostMapping
    public ResponseEntity<ProcedureDto> create(@RequestBody ProcedureDto procedureDto) {
        Procedure receivedProcedure = convertDtoToProcedure(procedureDto);
        procedureService.create(receivedProcedure);
        ProcedureDto createdProcedure = convertProcedureToDto(receivedProcedure);
        return new ResponseEntity<>(createdProcedure, HttpStatus.CREATED);
    }

    /**
     * Updating the procedure
     *
     * @param procedureDto to be updated
     * @param id           of the procedure
     * @return updated procedure
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProcedureDto> update(@RequestBody ProcedureDto procedureDto, @PathVariable Long id) {
        Procedure receivedProcedure = convertDtoToProcedure(procedureDto);
        procedureService.update(receivedProcedure, id);
        ProcedureDto updatedProcedure = convertProcedureToDto(receivedProcedure);
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
    public ResponseEntity<ProcedureDto> patch(@RequestBody Map<String, Object> fields, @PathVariable Long id) {
        Procedure patchedProcedure = procedureService.patch(fields, id);
        ProcedureDto patchedProcedureDto = convertProcedureToDto(patchedProcedure);
        return new ResponseEntity<>(patchedProcedureDto, HttpStatus.OK);
    }

    /**
     * Registering equipment for the procedure
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
        procedureService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Converting procedure to DTO
     *
     * @param procedure to be converted
     * @return ProcedureDTO object
     */
    private ProcedureDto convertProcedureToDto(Procedure procedure) {
        ProcedureDto procedureDto = modelMapper.map(procedure, ProcedureDto.class);
        procedureDto.convertLocalTimeToString(procedure.getDuration());
        return procedureDto;
    }

    /**
     * Converting ProcedureDto to Procedure
     *
     * @param procedureDto to be converted
     * @return Procedure object
     */
    private Procedure convertDtoToProcedure(ProcedureDto procedureDto) {
        Procedure procedure = modelMapper.map(procedureDto, Procedure.class);
        procedure.setDuration(procedureDto.convertStringToLocalTime());
        return procedure;
    }
}