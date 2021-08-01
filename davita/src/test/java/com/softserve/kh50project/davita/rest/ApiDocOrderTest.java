package com.softserve.kh50project.davita.rest;

import static java.util.Collections.singletonList;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.collectionToDelimitedString;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softserve.kh50project.davita.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;

import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.DavitaApplication;

import javax.persistence.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = DavitaApplication.class)
@TestPropertySource(locations = "classpath:application-test-order.properties")
public class ApiDocOrderTest {

    final static int TEST_LIST_SIZE = 5;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private List<Order> orders;
    private List<Doctor> doctors;
    private List<Patient> patients;
    private List<Procedure> procedures;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    void doctorsInit(int count) {
        doctors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Doctor doctor = new Doctor();
            doctor.setLogin("Login" + i);
            doctor.setPassword("Password" + i);
            doctor.setName("DoctorName" + i);
            doctor.setLastName("DoctorLastName" + i);
            doctor.setPhone("050505050" + i);
            doctor.setEmail("doctor" + i + "@gmail.com");
            doctor.setSpecialization("Specialization" + i);
            doctor.setCertificateNumber("0000000000" + i);
            doctors.add(doctor);
        }
    }

    void patientsInit(int count) {
        patients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Patient patient = new Patient();
            patient.setLogin("Login" + i);
            patient.setPassword("Password" + i);
            patient.setName("patientName" + i);
            patient.setLastName("patientLastName" + i);
            patient.setPhone("050999999" + i);
            patient.setEmail("patient" + i + "@gmail.com");
            patient.setInsuranceNumber("111111110" + i);
            patients.add(patient);
        }
    }

    void proceduresInit(int count) {
        procedures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Procedure procedure = new Procedure();
            procedure.setName("procedureName" + i);
            procedure.setCost(100.0 * (1 + i));
            procedure.setDuration(LocalTime.of(i, i * 5, i * 5));
            procedures.add(procedure);
        }
    }

    void ordersInit(int count) {
        orders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            //2021-07-01T10:15:00   2021-07-06T10:15:00
            order.setStart(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:15:00"));
            order.setFinish(LocalDateTime.parse("2021-07-0" + (i + 1) + "T10:30:00"));
            order.setCost(100.0 * (i + 1));
            order.setDoctor(doctors.get(i));
            order.setPatient(patients.get(i));
            order.setProcedure(procedures.get(i));
            orders.add(order);
        }
    }

    @BeforeEach
    void setUp() {
        doctorsInit(TEST_LIST_SIZE);
        proceduresInit(TEST_LIST_SIZE);
        patientsInit(TEST_LIST_SIZE);
        ordersInit(TEST_LIST_SIZE);
    }

//    @Test
//    public void crudGetExample() throws Exception {
//
//        Map<String, Object> crud = new HashMap<>();
//        crud.put("id", 1);
//        crud.put("name", "Sample Model");
//
//        String tagLocation = this.mockMvc
//                .perform(get("/equipment")
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(crud)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getHeader("Location");
//
//        crud.put("tags", singletonList(tagLocation));
//        ConstraintDescriptions desc = new ConstraintDescriptions(Equipment.class);
//
//        this.mockMvc
//                .perform(get("/equipment")
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(crud)))
//                .andExpect(status().isOk())
//                .andDo(document(
//                        "equipment-get-example",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("id").description("The id of the input" + collectionToDelimitedString(desc.descriptionsForProperty("id"), ". ")),
//                                fieldWithPath("name").description("The title of the input"),
//                                fieldWithPath("tags").description("An array of tag resource URIs")))
//                );
//    }

    @Test
    @DisplayName("Order : CRUD, create")
    public void crudOrderCreate() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("start", "2021-07-01T10:15:00");
        crud.put("finish", "2021-07-01T10:30:00");
        crud.put("cost", 100.00);
        crud.put("procedureId", 1L);
        crud.put("doctorId", 1L);
        crud.put("patientId", 1L);

        String tagLocation = this.mockMvc
            .perform(post("/orders")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(this.objectMapper.writeValueAsString(crud)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");
//        System.out.println(tagLocation);

//        this.mockMvc
//                .perform(get("/equipment")
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(crud)))
//                .andExpect(status().isOk())
//                .andDo(document(
//                        "equipment-get-example",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("id").description("The id of the input" + collectionToDelimitedString(desc.descriptionsForProperty("id"), ". ")),
//                                fieldWithPath("name").description("The title of the input"),
//                                fieldWithPath("tags").description("An array of tag resource URIs")))
//                );
//


    }

//    /**
//     * Getting order by id
//     *
//     * @param id order id
//     * @return the order by id
//     */
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<Order> readById(@PathVariable Long id) {
//        Order order = orderService.readById(id);
//        return new ResponseEntity<>(order, HttpStatus.OK);
//    }


    @Test
    @DisplayName("Order : CRUD, read by Id")
    public void crudOrderReadById() throws Exception {
        long findId = 1L;
        Map<String, Object> crud = new HashMap<>();
        crud.put("id", findId);

        this.mockMvc
            .perform(get("/orders/{id}",findId)
                    .contentType(MediaTypes.HAL_JSON)
                    .content(this.objectMapper.writeValueAsString(crud)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getHeader("Location");
    }



//    @Test
//    @Order(4)
//    public void crudDeleteExample() throws Exception {
//        this.mockMvc
//                .perform(delete("/equipment/{id}", 2))
//                .andExpect(status().isOk())
//                .andDo(document(
//                        "equipment-delete-example",
//                        pathParameters(parameterWithName("id").description("The id of the input to delete")))
//                );
//    }
//
//    @Test
//    @Disabled
//    public void crudPatchExample() throws Exception {
//
//        Map<String, String> tag = new HashMap<>();
//        tag.put("name", "PATCH");
//
//        String tagLocation = this.mockMvc
//                .perform(patch("/equipment/{id}", 2)
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(tag)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getHeader("Location");
//
//        Map<String, Object> crud = new HashMap<>();
//        crud.put("name", "Blood");
//        crud.put("tags", singletonList(tagLocation));
//        this.mockMvc
//                .perform(patch("/equipment/{id}", 2)
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(crud)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Order(3)
//    public void crudPutExample() throws Exception {
//        Map<String, Object> tag = new HashMap<>();
//        tag.put("equipment_id", 29L);
//        tag.put("name", "Blood Pressure");
//
//        String tagLocation = this.mockMvc
//                .perform(put("/equipment/{id}", 2L)
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(tag)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getHeader("Location");
//
//        Map<String, Object> crud = new HashMap<>();
//        crud.put("equipment_id", 2);
//        crud.put("name", "Blood");
//        crud.put("tags", singletonList(tagLocation));
//
//        this.mockMvc
//                .perform(put("/equipment/{id}", 2)
//                        .contentType(MediaTypes.HAL_JSON)
//                        .content(this.objectMapper.writeValueAsString(crud)))
//                .andExpect(status().isOk());
//    }
}