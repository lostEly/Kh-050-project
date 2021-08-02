package com.softserve.kh50project.davita.controller;

import com.softserve.kh50project.davita.DavitaApplication;
import com.softserve.kh50project.davita.dto.DoctorDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DavitaApplication.class)
class DoctorControllerTest {

    public static final String LOCALHOST = "http://localhost:";
    public static final String CONTEXT_PATH = "/ss-ita-davita-api";
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Order(1)
    @Test
    void getDoctorByIdWith404() {
        ResponseEntity<DoctorDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/doctors/1",
                HttpMethod.GET,
                null,
                DoctorDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Order(2)
    @Sql(scripts = "classpath:testdata/create-doctors.sql")
    @Test
    void getDoctorById() {
        ResponseEntity<DoctorDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/doctors/1",
                HttpMethod.GET,
                null,
                DoctorDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getUserId());
    }

    @Order(3)
    @Test
    void getDoctors() {
        ResponseEntity<List<DoctorDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Order(3)
    @Test
    void getDoctorsBySpecialization() {
        ResponseEntity<List<DoctorDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors?specialization=doctor1",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Order(4)
    @Test
    void createDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setName("test4");
        doctorDto.setLastName("tea");
        doctorDto.setSpecialization("doc");
        doctorDto.setCertificateNumber("12340");
        doctorDto.setDateOfBirthday(LocalDate.now());
        doctorDto.setEmail("df@xd.com");
        doctorDto.setLogin("sdfg");
        doctorDto.setPassword("erkjeklsa");
        doctorDto.setPhone("0940940930");

        HttpEntity<DoctorDto> entity = new HttpEntity<>(doctorDto, headers);

        ResponseEntity<DoctorDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors",
                        HttpMethod.POST,
                        entity,
                        DoctorDto.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getUserId());
    }

    @Order(5)
    @Test
    void updateDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setUserId(1L);
        doctorDto.setName("test-upd");
        doctorDto.setLastName("test");
        doctorDto.setSpecialization("doctor");
        doctorDto.setCertificateNumber("124345");
        doctorDto.setLogin("test");
        doctorDto.setPassword("pwd");

        HttpEntity<DoctorDto> entity = new HttpEntity<>(doctorDto, headers);

        ResponseEntity<DoctorDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/1",
                        HttpMethod.PUT,
                        entity,
                        DoctorDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test-upd", response.getBody().getName());
        assertNull(response.getBody().getEmail());
    }

    @Order(6)
    @Test
    void updateNotExistingDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<DoctorDto> entity = new HttpEntity<>(new DoctorDto(), headers);

        ResponseEntity<DoctorDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/99",
                        HttpMethod.PUT,
                        entity,
                        DoctorDto.class
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Order(7)
    @Test
    void patchDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> fields = Map.of("lastName", "lastename-upd");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(fields, headers);

        ResponseEntity<DoctorDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/2",
                        HttpMethod.PATCH,
                        entity,
                        DoctorDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("lastename-upd", response.getBody().getLastName());
    }

    @Order(8)
    @Test
    void patchNotExistingDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(Map.of(), headers);

        ResponseEntity<DoctorDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/9",
                        HttpMethod.PATCH,
                        entity,
                        DoctorDto.class
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Order(9)
    @Test
    void deleteDoctor() {
        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/1",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Order(10)
    @Test
    void deleteNotExistingDoctor() {
        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/doctors/99",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}