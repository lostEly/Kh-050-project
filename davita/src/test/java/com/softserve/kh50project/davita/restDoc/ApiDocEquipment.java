package com.softserve.kh50project.davita.restDoc;


import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.HashMap;
import java.util.Map;

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


import com.fasterxml.jackson.databind.ObjectMapper;

import com.softserve.kh50project.davita.DavitaApplication;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = DavitaApplication.class)
@TestPropertySource("classpath:application-test.properties")
public class ApiDocEquipment {


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
    public void crudCreateEquipment() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("name", "Hospital beds.");


        this.mockMvc.perform(post("/equipment").contentType(MediaTypes.COLLECTION_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andDo(document("equipment-create-example", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The name of the input"))));
    }



    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void crudPatchExample() throws Exception {

        Map<String, Object> crud = new HashMap<>();
        crud.put("name", "Hospital beds.");


        this.mockMvc.perform(patch("/equipment/{id}", 1).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("crud-patch-equipment", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The title of the input")
                        )));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void crudGetEquipment() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("name", null);

        this.mockMvc.perform(get("/equipment/{id}", 2).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("equipment-get-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The title of the input"))));
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void crudPutEquipment() throws Exception {

        Map<String, Object> crud = new HashMap<>();
        crud.put("equipment_id", 2);
        crud.put("name", "Pressure mattresses.");


        this.mockMvc.perform(put("/equipment/{id}", 2).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("equipment-put-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("equipment_id").description("The title of the input"),
                                fieldWithPath("name").description("The title of the input")
                        )));
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dataForTests/data-equipment.sql")
    public void crudDeleteEquipment() throws Exception {
        this.mockMvc.perform(delete("/equipment/{id}", 1))
                .andExpect(status().isOk())
                .andDo(document("equipment-delete-example", pathParameters(parameterWithName("id").description("The id of the input to delete"))));
    }
}