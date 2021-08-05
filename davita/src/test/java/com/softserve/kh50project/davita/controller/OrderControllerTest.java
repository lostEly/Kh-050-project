package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.dto.ProcedureDto;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.model.Procedure;
import com.softserve.kh50project.davita.service.impl.OrderServiceImpl;
import com.softserve.kh50project.davita.service.impl.ProcedureServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

//    @BeforeEach
//    @Sql(scripts = "classpath:testdata/create-orders.sql")
//    void initTables(){ }

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

        mockMvc.perform(put("/orders/99")
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
    void deleteNotExistingOrder() throws Exception {
        mockMvc.perform(delete("/orders/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:testdata/create-orders.sql")
    @DisplayName("GET: order filter")
    void getOrderFilter() throws Exception {
        String param1 = "start=2021-07-02T00:00:01";
        String param2 = "finish=2021-07-07T23:59:59";
        String param3 = "procedureId=2";

        mockMvc.perform(get("/orders/filter?"+param1+"&"+param2+"&"+param3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].procedureId").value(2))
                .andExpect(jsonPath("$[1].procedureId").value(2))
                .andExpect(jsonPath("$[2].procedureId").value(2));
    }

    @Test
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
//
//    @Test
//    void doctorNextDayCalendar() throws Exception {
//        String startStr = "2021-07-02T00:00:01";
//        LocalDateTime start = LocalDateTime.parse(startStr);
//        LocalDateTime finish = start.plusDays(1).withHour(23).withMinute(59).withSecond(59);
//        String finishStr = finish.toString();
//
//        Map<String, Object> fieldsMap = new HashMap();
//        fieldsMap.put("start",startStr);
//        fieldsMap.put("finish", finishStr);
//        fieldsMap.put("doctorId", 2L);
//
//        mockMvc.perform(get("/orders")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(fieldsMap)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))//3
//                .andExpect(jsonPath("$[0].doctorId", is(2)))
//                .andExpect(jsonPath("$[1].doctorId", is(2)))
//                .andExpect(status().isOk());
//    }

}