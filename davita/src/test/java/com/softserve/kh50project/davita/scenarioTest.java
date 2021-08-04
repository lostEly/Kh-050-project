package com.softserve.kh50project.davita;

import com.softserve.kh50project.davita.dto.DoctorDto;
import com.softserve.kh50project.davita.dto.EquipmentDto;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.PatientDto;
import com.softserve.kh50project.davita.model.Procedure;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DavitaApplication.class)
public class scenarioTest {
    public static final String LOCALHOST = "http://localhost:";
    public static final String CONTEXT_PATH = "/ss-ita-davita-api";
    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();


    @Test
    @Order(1)
    @DisplayName("get doctor by id")
    @Sql(scripts = "classpath:dataForTests/scenario.sql")
    void getDoctorById() {
        ResponseEntity<DoctorDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/doctors/2",
                HttpMethod.GET,
                null,
                DoctorDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getUserId());

    }


    @Test
    @Order(2)
    @DisplayName("get patient by id")
    void getPatientById() {
        ResponseEntity<PatientDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/patients/4",
                HttpMethod.GET,
                null,
                PatientDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().getUserId());
        assertEquals("alien", response.getBody().getLogin());
        assertEquals("Mason", response.getBody().getName());
    }


    @Test
    @Order(3)
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
        assertEquals(2, response.getBody().size());
    }


    @Test
    @Order(4)
    void getPatients() {
        ResponseEntity<List<PatientDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/patients",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @Order(5)
    void checkNotExistProcedures() {
        ResponseEntity<OrderDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders/1",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    @Order(6)
    @DisplayName("Create free orders(slots)")
    void createEmptyOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setCost(40.0);
        orderDto.setStart("2021-08-09T08:00:00");
        orderDto.setFinish("2021-08-09T09:00:00");
        orderDto.setDoctorId(null);
        orderDto.setPatientId(null);
        orderDto.setProcedureId(2L);

        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, headers);

        ResponseEntity<OrderDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders",
                        HttpMethod.POST,
                        entity,
                        OrderDto.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().getOrderId());
        assertEquals(null, response.getBody().getDoctorId());
        assertEquals(null, response.getBody().getPatientId());


    }


/*    @Test
    @Order(4)
    @DisplayName("Check that second doctor is empty")
    void getOrder() {
        ResponseEntity<OrderDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders/2",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(20.0, response.getBody().getCost());
        assertEquals(null, response.getBody().getDoctorId());

    }*/


    @Test
    @Order(7)
    @DisplayName("Assign doctor with an empty order")
    void updateDoctor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setCost(40.0);
        orderDto.setStart("2021-08-09T08:00:00");
        orderDto.setFinish("2021-08-09T09:00:00");
        orderDto.setDoctorId(1L);
        orderDto.setPatientId(4L);
        orderDto.setProcedureId(2L);

        HttpEntity<OrderDto> entity = new HttpEntity<>(orderDto, headers);

        ResponseEntity<OrderDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders/1",
                        HttpMethod.PUT,
                        entity,
                        OrderDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getDoctorId());
        assertEquals(4, response.getBody().getPatientId());

    }


    @Test
    @Order(8)
    void getOrderById() {
        ResponseEntity<OrderDto> response = restTemplate.exchange(
                LOCALHOST + port + CONTEXT_PATH + "/orders/1",
                HttpMethod.GET,
                null,
                OrderDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getDoctorId());
        assertEquals(4, response.getBody().getPatientId());

    }


    @Order(9)
    @Test
    void patchOrderTime() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> fields = new HashMap<>();
        fields.put("start", "2021-08-09T10:00");
        fields.put("finish", "2021-08-09T12:00");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(fields, headers);

        ResponseEntity<OrderDto> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders/1",
                        HttpMethod.PATCH,
                        entity,
                        OrderDto.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2021-08-09T10:00", response.getBody().getStart());
        assertEquals("2021-08-09T12:00", response.getBody().getFinish());
    }




    @Order(10)
    @Test
    void deleteOrder() {
        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + CONTEXT_PATH + "/orders/1",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
