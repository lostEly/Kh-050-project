package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.PatientDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("classpath:application-test-order.properties")
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Order(1)
    @Test
    void getOrderById404() throws Exception {
        mockMvc.perform(get("/orders/1001"))
                .andExpect(status().isNotFound());
    }

    @Order(2)
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @Test
    void getOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));
    }

    @Order(3)
    @Test
    void getOrdersAll() throws Exception {

        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))//3
                .andExpect(jsonPath("$[0].orderId", is(1)))
                .andExpect(jsonPath("$[1].orderId", is(2)))
                .andExpect(jsonPath("$[2].orderId", is(3)))
                .andExpect(status().isOk());
    }

    @Order(4)
    @Test
    void createNewOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setStart("2021-07-01T10:15:00");
        orderDto.setFinish("2021-07-01T10:30:00");
        orderDto.setCost(100.0);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", notNullValue()));
    }

    @Order(5)
    @Test
    void updateOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setStart("2021-07-01T01:01:01");
        orderDto.setFinish("2021-07-01T11:11:11");
        orderDto.setCost(999.0);

        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value("2021-07-01T01:01:01"))
                .andExpect(jsonPath("$.finish").value("2021-07-01T11:11:11"))
                .andExpect(jsonPath("$.cost").value(999.0))
                .andExpect(status().isOk());
    }

    @Order(6)
    @Test
    void updateNotExistingPatient() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setStart("2021-07-01T01:01:01");
        orderDto.setFinish("2021-07-01T11:11:11");
        orderDto.setCost(999.0);

        mockMvc.perform(put("/orders/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isNotFound());
    }

    @Order(7)
    @Test
    void patchOrder() throws Exception {
        Map<String, Object> fields = Map.of("cost", 99999.0);

        mockMvc.perform(patch("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost").value(99999.0))
                .andExpect(status().isOk());
    }

    @Order(8)
    @Test
    void patchNotExistingOrder() throws Exception {
        mockMvc.perform(patch("/orders/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isNotFound());
    }

    @Order(9)
    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isOk());
    }

    @Order(10)
    @Test
    void deleteNotExistingPatient() throws Exception {
        mockMvc.perform(delete("/orders/9999"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Order(11)
    @Test
    void getEmptyDoctor() throws Exception {
        Map<String, Object> fieldsMap = new HashMap();
        fieldsMap.put("start", "2021-07-02T00:00:01");
        fieldsMap.put("finish", "2021-07-07T23:59:59");
        fieldsMap.put("procedureId", 2L);

        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))//3
                .andExpect(jsonPath("$[0].orderId", is(2)))
                .andExpect(jsonPath("$[1].orderId", is(4)))
                .andExpect(jsonPath("$[2].orderId", is(6)))
                .andExpect(status().isOk());
    }

    @Order(12)
    @Test
    void bookOrders() throws Exception {
        Long[] idArr = {5L, 2L, 3L, 4L};
        mockMvc.perform(patch("/orders/bookOrdersForDoctor/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(idArr))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))//3
                .andExpect(jsonPath("$[0].doctorId", is(2)))
                .andExpect(jsonPath("$[1].doctorId", is(2)))
                .andExpect(jsonPath("$[2].doctorId", is(2)))
                .andExpect(jsonPath("$[3].doctorId", is(2)))
                .andExpect(status().isOk());
    }

    @Order(12)
    @Test
    void doctorNextDayCalendar() throws Exception {
        String startStr = "2021-07-02T00:00:01";
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime finish = start.plusDays(1).withHour(23).withMinute(59).withSecond(59);
        String finishStr = finish.toString();

        Map<String, Object> fieldsMap = new HashMap();
        fieldsMap.put("start",startStr);
        fieldsMap.put("finish", finishStr);
        fieldsMap.put("doctorId", 2L);

        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fieldsMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))//3
                .andExpect(jsonPath("$[0].doctorId", is(2)))
                .andExpect(jsonPath("$[1].doctorId", is(2)))
                .andExpect(status().isOk());
    }

}