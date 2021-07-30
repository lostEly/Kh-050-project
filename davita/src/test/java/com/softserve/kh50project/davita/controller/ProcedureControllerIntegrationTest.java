package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.repository.EquipmentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application-test.properties")
class ProcedureControllerIntegrationTest {
    private static ProcedureDto procedureDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @BeforeAll
    public static void initProcedure() {
        procedureDto = new ProcedureDto();
        procedureDto.setName("procedure");
        procedureDto.setCost(1.0);
        procedureDto.convertLocalTimeToString(LocalTime.of(1, 1, 1));
    }

    @Test
    @Order(1)
    void create() throws Exception {

        MvcResult result = mockMvc.perform(post("http://localhost:8080/procedures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(procedureDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        ProcedureDto actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(actual);
        assertNotNull(actual.getProcedureId());
        assertEquals(procedureDto.getName(), actual.getName());
        procedureDto = actual;
    }

    @Test
    @Order(2)
    void update() throws Exception {
        procedureDto.setName("updatedName");
        MvcResult result = mockMvc.perform(put("http://localhost:8080/procedures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(procedureDto)))
                .andExpect(status().isOk())
                .andReturn();
        ProcedureDto actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(actual);
        assertEquals(procedureDto.getName(), actual.getName());
    }

    @Test
    @Order(3)
    void patch() throws Exception {
        String patchedName = "patchedName";
        Double patchedCost = 2d;
        procedureDto.setName(patchedName);
        procedureDto.setCost(patchedCost);

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("name", patchedName);
        fields.put("cost", patchedCost);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/procedures/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(fields)))
                .andExpect(status().isOk())
                .andReturn();
        ProcedureDto actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(actual);
        assertEquals(procedureDto.getName(), actual.getName());
        assertEquals(procedureDto.getCost(), actual.getCost());
    }

    @Test
    @Order(4)
    void registerEquipment() throws Exception {
        Equipment equipment = new Equipment();
        equipment.setName("equipment");
        equipmentRepository.save(equipment);
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/procedures/1/register-equipment/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void readById() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/procedures/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        ProcedureDto actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(actual);
        assertEquals(procedureDto.getName(), actual.getName());
    }

    @Test
    @Order(6)
    void read() throws Exception {
        List<ProcedureDto> list = new ArrayList<>();
        list.add(procedureDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/procedures"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        List<ProcedureDto> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(list.size(), actual.size());
    }

    @Test
    @Order(7)
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/procedures/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}