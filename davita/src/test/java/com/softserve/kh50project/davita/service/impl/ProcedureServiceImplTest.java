package com.softserve.kh50project.davita.service.impl;

import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.exceptions.ResourceNotFoundException;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.repository.ProcedureRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.criteria.Predicate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class ProcedureServiceImplTest {

    private static Procedure procedure;
    private static ProcedureDto procedureDto;

    @Autowired
    private ProcedureServiceImpl procedureService;

    @MockBean
    private ProcedureRepository procedureRepository;

    @BeforeAll
    public static void initProcedure() {
        procedure = new Procedure();
        procedure.setProcedureId(1L);
        procedure.setName("procedure");
        procedure.setCost(1.0);
        procedure.setDuration(LocalTime.of(1, 1, 1));
        procedureDto = new ProcedureDto();
        procedureDto.setProcedureId(1L);
        procedureDto.setName("procedure");
        procedureDto.setCost(1.0);
        procedureDto.convertLocalTimeToString(LocalTime.of(1, 1, 1));
    }

    @BeforeEach
    public void setUp() {
        Mockito.doReturn(Optional.of(procedure))
                .when(procedureRepository)
                .findById(anyLong());
        Mockito.doReturn(procedure)
                .when(procedureRepository)
                .save(procedure);
    }

    @Test
    void readById() {
        Long id = 1L;
        ProcedureDto foundProcedure = procedureService.readById(id);
        Mockito.verify(procedureRepository)
                .findById(anyLong());
        assertNotNull(foundProcedure);
        assertEquals(id, foundProcedure.getProcedureId());
    }

    @Test
    @DisplayName("Read by id throws an exception if argument is null")
    void readById1() {
        assertThrows(ResourceNotFoundException.class, () -> procedureService.readById(null));
    }

    @Test
    @DisplayName("Read returns list with single mock with all null args")
    void read() {
        Specification<Procedure> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Mockito.when(procedureRepository.getProcedureQuery(null, null, null))
                .thenReturn(specification);
        Mockito.when(procedureRepository.findAll(specification))
                .thenReturn(Collections.singletonList(procedure));
        List<ProcedureDto> procedureDtoList = procedureService.read(null, null, null);
        Mockito.verify(procedureRepository)
                .getProcedureQuery(null, null, null);
        Mockito.verify(procedureRepository)
                .findAll(specification);
        assertNotNull(procedureDtoList);
        assertEquals(1, procedureDtoList.size());
    }

    @Test
    void create() {
        ProcedureDto procedureDto1 = procedureService.create(procedureDto);
        Mockito.verify(procedureRepository)
                .save(procedure);
        assertNotNull(procedureDto1);
        assertEquals("procedure", procedureDto1.getName());
    }

    @Test
    void update() {
        String updatedName = "updatedProcedure";
        ProcedureDto procedureDto1 = procedureDto;
        procedureDto1.setName(updatedName);
        ProcedureDto updatedProcedure = procedureService.update(procedureDto1, procedureDto1.getProcedureId());
        Mockito.verify(procedureRepository)
                .save(procedureService.convertDtoToProcedure(procedureDto1));
        assertNotNull(updatedProcedure);
        assertEquals(updatedName, updatedProcedure.getName());
    }

    @Test
    void patch() {
        Map<String, Object> fields = new LinkedHashMap<>();
        String updatedName = "patchedProcedure";
        fields.put("name", updatedName);
        ProcedureDto procedureDto1 = procedureDto;
        ProcedureDto patchedProcedureDto = procedureService.patch(fields, procedureDto1.getProcedureId());
        Mockito.verify(procedureRepository)
                .findById(anyLong());
        Mockito.verify(procedureRepository)
                .save(procedure);
        assertNotNull(patchedProcedureDto);
        assertEquals(updatedName, patchedProcedureDto.getName());
    }

    @Test
    void delete() {
        procedureService.delete(procedure.getProcedureId());
        Mockito.verify(procedureRepository)
                .deleteById(anyLong());
    }

    @Test
    void convertProcedureToDto() {
        ProcedureDto procedureDto = procedureService.convertProcedureToDto(procedure);
        assertEquals(procedureDto.getProcedureId(), procedure.getProcedureId());
        assertEquals(procedureDto.getName(), procedure.getName());
        assertEquals(procedureDto.getCost(), procedure.getCost());
        assertEquals(procedureDto.convertStringToLocalTime(), procedure.getDuration());
    }

    @Test
    void convertDtoToProcedure() {
        Procedure procedure = procedureService.convertDtoToProcedure(procedureDto);
        assertEquals(procedureDto.getProcedureId(), procedure.getProcedureId());
        assertEquals(procedureDto.getName(), procedure.getName());
        assertEquals(procedureDto.getCost(), procedure.getCost());
        assertEquals(procedureDto.convertStringToLocalTime(), procedure.getDuration());
    }
}