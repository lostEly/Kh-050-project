package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.service.impl.ProcedureServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcedureController.class)
@TestPropertySource("/application-test.properties")
class ProcedureControllerUnitTest {
    private static ProcedureDto procedureDto;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProcedureServiceImpl procedureService;

    @BeforeAll
    public static void initProcedure() {
        procedureDto = new ProcedureDto();
        procedureDto.setProcedureId(1L);
        procedureDto.setName("procedure");
        procedureDto.setCost(1.0);
        procedureDto.convertLocalTimeToString(LocalTime.of(1, 1, 1));
    }

    @Test
    void read() throws Exception {
        List<ProcedureDto> list = new ArrayList<>();
        list.add(procedureDto);
        Mockito.when(procedureService.read(null, null, null))
                .thenReturn(list);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/procedures"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        List<ProcedureDto> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(1, actual.size());
    }

    @Test
    void readById() throws Exception {
        Mockito.when(procedureService.readById(1L))
                .thenReturn(procedureDto);
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
    void create() throws Exception {
        Mockito.when(procedureService.create(any(ProcedureDto.class)))
                .thenReturn(procedureDto);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/procedures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(procedureDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        ProcedureDto actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(actual);
        assertEquals(procedureDto.getName(), actual.getName());
    }

    @Test
    void update() throws Exception {
        procedureDto.setName("updatedName");
        Mockito.when(procedureService.update(any(ProcedureDto.class), anyLong()))
                .thenReturn(procedureDto);

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
    void patch() throws Exception {
        String patchedName = "patchedName";
        Double patchedCost = 2d;
        procedureDto.setName(patchedName);
        procedureDto.setCost(patchedCost);
        Mockito.when(procedureService.update(any(ProcedureDto.class), anyLong()))
                .thenReturn(procedureDto);

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("name", patchedName);
        fields.put("cost", patchedCost);

        MvcResult result = mockMvc.perform(put("http://localhost:8080/procedures/1")
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
    void registerEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/procedures/1/register-equipment/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/procedures/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}