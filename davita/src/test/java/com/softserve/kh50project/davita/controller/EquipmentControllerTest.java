package com.softserve.kh50project.davita.controller;


import com.softserve.kh50project.davita.DavitaApplication;
import com.softserve.kh50project.davita.dto.EquipmentDto;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DavitaApplication.class)
public class EquipmentControllerTest {

    public static final String LOCALHOST = "http://localhost:";
    public static final String CONTEXT_PATH = "/ss-ita-davita-api";
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();


    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void getEquipmentById() {
        ResponseEntity<EquipmentDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/equipment/1",
                HttpMethod.GET,
                null,
                EquipmentDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getEquipmentId());
    }

    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void getEquipmentWith404() {
        ResponseEntity<EquipmentDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/equipment/10",
                HttpMethod.GET,
                null,
                EquipmentDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void getListOfEquipment() {
        ResponseEntity<List<EquipmentDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/equipment",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
    }

    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void getListNamePredicate() {
        ResponseEntity<List<EquipmentDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/equipment?name=Equipment",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void createEquipment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("pumps");

        HttpEntity<EquipmentDto> equipmentDtoHttpEntity = new HttpEntity<>(equipmentDto, headers);
        ResponseEntity<EquipmentDto> responseEntity = restTemplate.exchange(LOCALHOST
                        + port + CONTEXT_PATH + "/equipment",
                HttpMethod.POST,
                equipmentDtoHttpEntity,
                EquipmentDto.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getEquipmentId());
    }

    @Test
    void updatePutMethodEquipment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept((List.of(MediaType.APPLICATION_JSON)));

        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setEquipmentId(1L);
        equipmentDto.setName("Medical mattress");

        HttpEntity<EquipmentDto> equipmentDtoHttpEntity = new HttpEntity<>(equipmentDto, headers);

        ResponseEntity<EquipmentDto> responseEntity = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/equipment/1",
                HttpMethod.PUT,
                equipmentDtoHttpEntity,
                EquipmentDto.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Medical mattress", responseEntity.getBody().getName());

    }


    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void updatePatchMethodEquipment() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept((List.of(MediaType.APPLICATION_JSON)));

        Map<String, Object> fields = Map.of("name", "Medical mattress");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(fields, headers);

        ResponseEntity<EquipmentDto> responseEntity = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/equipment/1",
                HttpMethod.PATCH,
                entity,
                EquipmentDto.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Medical mattress", responseEntity.getBody().getName());
    }

    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void deleteEquipment() {
        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/equipment/1",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Sql(scripts = "classpath:dataForTests/data-equipment.sql")
    void deleteNotExisting() {
        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/equipment/99",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
