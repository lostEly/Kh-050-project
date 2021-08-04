package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.PatientDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void getPatientByIdWith404() throws Exception {
        mockMvc.perform(get("/patients/34"))
                .andExpect(status().isNotFound());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void getPatientById() throws Exception {
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void getPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[2].userId", is(3)))
                .andExpect(status().isOk());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void getPatientsByName() throws Exception {
        mockMvc.perform(get("/patients?lastName=test11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(2)))
                .andExpect(jsonPath("$[1].userId", is(3)))
                .andExpect(status().isOk());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void createPatient() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setName("test4");
        patientDto.setLastName("tea");
        patientDto.setInsuranceNumber("42069228322");
        patientDto.setDateOfBirthday(LocalDate.now());
        patientDto.setEmail("df@xd.com");
        patientDto.setLogin("sdfg");
        patientDto.setPassword("erkjeklsa");
        patientDto.setPhone("0940940930");

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", notNullValue()));
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void updatePatient() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setUserId(1L);
        patientDto.setName("test-upd");
        patientDto.setLastName("test");
        patientDto.setInsuranceNumber("1243453");
        patientDto.setLogin("test");
        patientDto.setPassword("pwd");

        mockMvc.perform(put("/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test-upd"))
                .andExpect(jsonPath("$.email", nullValue()));
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void updateNotExistingPatient() throws Exception {
        mockMvc.perform(put("/patients/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PatientDto())))
                .andExpect(status().isNotFound());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void patchPatient() throws Exception {
        Map<String, Object> fields = Map.of("lastName", "lastname-upd");

        mockMvc.perform(patch("/patients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("lastname-upd"));
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void patchNotExistingPatient() throws Exception {
        mockMvc.perform(patch("/patients/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isNotFound());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void deletePatient() throws Exception {
        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isOk());
    }

    @Sql(scripts = "classpath:testdata/create-patients.sql")
    @Test
    void deleteNotExistingPatient() throws Exception {
        mockMvc.perform(delete("/patients/99"))
                .andExpect(status().isOk())
                .andReturn();
    }
}