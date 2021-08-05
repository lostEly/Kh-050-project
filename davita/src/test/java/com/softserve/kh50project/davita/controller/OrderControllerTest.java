package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.OrderDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: by Id (404)")
    void getOrderById404() throws Exception {
        mockMvc.perform(get("/orders/1001"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: by Id")
    void getOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: all")
    void getOrdersAll() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("POST: new order")
    void createNewOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setStart("2021-07-01T10:15:00");
        orderDto.setFinish("2021-07-01T10:30:00");
        orderDto.setCost(100.0);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", notNullValue()))
                .andExpect(jsonPath("$.start").value("2021-07-01T10:15:00"))
                .andExpect(jsonPath("$.finish").value("2021-07-01T10:30:00"))
                .andExpect(jsonPath("$.cost").value(100.0));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PUT: update order")
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
                .andExpect(jsonPath("$.cost").value(999.0));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PUT: update order 404")
    void updateNotExistingOrder() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setStart("2021-07-01T01:01:01");
        orderDto.setFinish("2021-07-01T11:11:11");
        orderDto.setCost(999.0);

        mockMvc.perform(put("/orders/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PATCH: update order")
    void patchOrder() throws Exception {
        Map<String, Object> fields = Map.of("cost", 99999.0);

        mockMvc.perform(patch("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.cost").value(99999.0));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PATCH: update order 404")
    void patchNotExistingOrder() throws Exception {
        mockMvc.perform(patch("/orders/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("DELETE: order by id")
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("DELETE: order by id 404")
    void deleteOrder404() throws Exception {
        mockMvc.perform(delete("/orders/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: order filter")
    void getOrderFilter() throws Exception {
        String param1 = "start=2021-08-05T00:00:01";
        String param2 = "finish=2021-08-09T23:59:59";
        String param3 = "procedureId=2";

        mockMvc.perform(get("/orders/filter?"+param1+"&"+param2+"&"+param3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].procedureId").value(2))
                .andExpect(jsonPath("$[1].procedureId").value(2))
                .andExpect(jsonPath("$[2].procedureId").value(2));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PATCH: reserve orders")
    void bookOrders() throws Exception {
        String idStringList =2+","+3+","+4+","+5;
        mockMvc.perform(patch("/orders/bookOrdersForDoctor/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(idStringList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))//3
                .andExpect(jsonPath("$[0].doctorId", is(2)))
                .andExpect(jsonPath("$[1].doctorId", is(2)))
                .andExpect(jsonPath("$[2].doctorId", is(2)))
                .andExpect(jsonPath("$[3].doctorId", is(2)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("PATCH: reserve orders 404")
    void bookOrders404() throws Exception {
        String idStringList =200+","+300+","+400+","+500;
        mockMvc.perform(patch("/orders/bookOrdersForDoctor/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(idStringList)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: doctor calendar")
    void doctorNextDayCalendar() throws Exception {
        mockMvc.perform(get("/orders/doctor-calendar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))//3
                .andExpect(jsonPath("$[0].doctorId", is(1)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: doctor calendar 404")
    void doctorNextDayCalendar404() throws Exception {
        mockMvc.perform(get("/orders/doctor-calendar/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: all patient orders")
    void patientOrders() throws Exception {
        mockMvc.perform(get("/orders/appointments/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))//3
                .andExpect(jsonPath("$[0].patientId", is(3)))
                .andExpect(jsonPath("$[1].patientId", is(3)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: all patient orders 404")
    void patientOrders404() throws Exception {
        mockMvc.perform(get("/orders/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: all doctor orders")
    void doctorOrders() throws Exception {
        mockMvc.perform(get("/orders/doctor-appointments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].doctorId", is(2)))
                .andExpect(jsonPath("$[1].doctorId", is(2)))
                .andExpect(jsonPath("$[2].doctorId", is(2)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: all doctor orders 404")
    void doctorOrders404() throws Exception {
        mockMvc.perform(get("/orders/doctor-appointments/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: free orders for patient")
    void patientFreeOrders() throws Exception {
        mockMvc.perform(get("/orders/free/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))//3
                .andExpect(jsonPath("$[0].procedureId", is(2)))
                .andExpect(jsonPath("$[1].procedureId", is(2)));
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: free orders for patient")
    void patientFreeOrders404() throws Exception {
        mockMvc.perform(get("/orders/free/333333"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: free orders for doctor")
    void doctorFreeOrders() throws Exception {
        mockMvc.perform(get("/orders/doctor-free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

}