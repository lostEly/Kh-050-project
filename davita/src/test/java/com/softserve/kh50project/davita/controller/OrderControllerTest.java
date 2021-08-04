package com.softserve.kh50project.davita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.dto.OrderDto;
import com.softserve.kh50project.davita.model.Doctor;
import com.softserve.kh50project.davita.model.Order;
import com.softserve.kh50project.davita.model.Patient;
import com.softserve.kh50project.davita.model.Procedure;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource("classpath:application-test-order.properties")
@AutoConfigureMockMvc
@SpringBootTest

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test-order.properties")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    Doctor createDoctor(int pos) {
        Doctor doctor = new Doctor();
        doctor.setLogin("Login" + pos);
        doctor.setPassword("Password" + pos);
        doctor.setName("DoctorName" + pos);
        doctor.setLastName("DoctorLastName" + pos);
        doctor.setPhone("050505050" + pos);
        doctor.setEmail("doctor" + pos + "@gmail.com");
        doctor.setSpecialization("Specialization" + pos);
        doctor.setCertificateNumber("0000000000" + pos);
        return entityManager.persistAndFlush(doctor);
    }

    Patient createPatient(int pos) {
        Patient patient = new Patient();
        patient.setLogin("Login" + pos);
        patient.setPassword("Password" + pos);
        patient.setName("patientName" + pos);
        patient.setLastName("patientLastName" + pos);
        patient.setPhone("050999999" + pos);
        patient.setEmail("patient" + pos + "@gmail.com");
        patient.setInsuranceNumber("111111110" + pos);
        return entityManager.persistAndFlush(patient);
    }

    Procedure createProcedure(int pos) {
        Procedure procedure = new Procedure();
        procedure.setName("procedureName" + pos);
        procedure.setCost(100.0 * (1 + pos));
        procedure.setDuration(LocalTime.of(pos, pos * 5, pos * 5));
        return entityManager.persistAndFlush(procedure);
    }

    void ordersInit(int count) {
    }


    @BeforeEach
//    @Sql(scripts = "classpath:testdata/create-orders.sql")
    void initTables(){
        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setStart(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:15:00"));
            order.setFinish(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:30:00"));
            order.setCost(100.0 * (i + 1));

            order.setDoctor(createDoctor(i));
            order.setPatient(createPatient(i));
            order.setProcedure(createProcedure(i));
            entityManager.persistAndFlush(order);
        }
    }

    @AfterEach
//    @Sql(scripts = "classpath:testdata/delete-orders.sql")
    void dropTable(){
        entityManager.clear();
    }

    @Test
    void getOrderById404() throws Exception {
        mockMvc.perform(get("/orders/1001"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));
    }

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

    @Test
    void patchNotExistingOrder() throws Exception {
        mockMvc.perform(patch("/orders/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of())))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder() throws Exception {
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNotExistingPatient() throws Exception {
        mockMvc.perform(delete("/orders/9999"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

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