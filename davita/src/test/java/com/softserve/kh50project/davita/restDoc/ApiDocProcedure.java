package com.softserve.kh50project.davita.restDoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.DavitaApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = DavitaApplication.class)
@TestPropertySource("classpath:application-test.properties")
public class ApiDocProcedure {
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    @Disabled
    public void indexExample() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(document("index-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), links(linkWithRel("procedure").description("The CRUD resource")), responseFields(subsectionWithPath("_links").description("Links to other resources")),
                        responseHeaders(headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`"))));
    }


    @Test
    @Order(1)
    public void crudCreateProcedure() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("cost", 10.20);
        crud.put("duration", LocalTime.now());
        crud.put("name", "Blood test");


        this.mockMvc.perform(post("/procedures").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andDo(document("procedures-create-example", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The name of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input"))));
    }


    @Test
    @Order(2)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-procedure.sql")
    public void crudDeleteProcedure() throws Exception {
        this.mockMvc.perform(delete("/procedures/{id}", 5))
                .andExpect(status().isOk())
                .andDo(document("procedures-delete-example", pathParameters(parameterWithName("id").description("The id of the input to delete"))));
    }


    @Test
    @Order(3)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-procedure.sql")
    public void crudPutProcedure() throws Exception {

        Map<String, Object> crud = new HashMap<>();
        crud.put("procedureId", "3");
        crud.put("name", "Echocardiogram");
        crud.put("duration", "20:15:30");
        crud.put("cost", 210.40);


        this.mockMvc.perform(put("/procedures/{id}", 3).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("procedures-put-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("procedureId").description("The title of the input"),
                                fieldWithPath("name").description("The title of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input")
                        )));
    }


    @Test
    @Order(4)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-procedure.sql")
    public void crudGetProcedure() throws Exception {

        Map<String, Object> crud = new HashMap<>();//for useful payload
        crud.put("name", null);
        crud.put("duration", null);
        crud.put("cost", 0.0);


        this.mockMvc.perform(get("/procedures/{id}", 5).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("procedures-get-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(

                                fieldWithPath("name").description("The title of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input"))));
    }


    @Test
    @Order(5)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-procedure.sql")
    public void crudPatchProcedure() throws Exception {

        Map<String, Object> crud = new HashMap<>();//for useful payload
        crud.put("name", "Echocardiogram");
        crud.put("cost", 210.40);


        this.mockMvc.perform(patch("/procedures/{id}", 5).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("procedures-patch-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The title of the input"),
                                fieldWithPath("cost").description("The cost of the input"))));
    }


}


