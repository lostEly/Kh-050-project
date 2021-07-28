package com.softserve.kh50project.davita;


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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.collectionToDelimitedString;


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
import org.springframework.restdocs.constraints.ConstraintDescriptions;

import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/*
import com.baeldung.restdocs.CrudInput;
import com.baeldung.restdocs.SpringRestDocsApplication;*/
import com.fasterxml.jackson.databind.ObjectMapper;

import com.softserve.kh50project.davita.model.Equipment;
import com.softserve.kh50project.davita.DavitaApplication;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = DavitaApplication.class)
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
    @Disabled
    public void indexExample() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(document("index-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), links(linkWithRel("equipment").description("The CRUD resource")), responseFields(subsectionWithPath("_links").description("Links to other resources")),
                        responseHeaders(headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`"))));
    }

    @Test
    @Order(2)
    public void crudGetExample() throws Exception {

        Map<String, Object> crud = new HashMap<>();
        crud.put("id", 1);
        crud.put("name", "Sample Model");

        String tagLocation = this.mockMvc.perform(get("/equipment").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        crud.put("tags", singletonList(tagLocation));

        ConstraintDescriptions desc = new ConstraintDescriptions(Equipment.class);

        this.mockMvc.perform(get("/equipment").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("equipment-get-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("id").description("The id of the input" + collectionToDelimitedString(desc.descriptionsForProperty("id"), ". ")),
                                fieldWithPath("name").description("The title of the input"), fieldWithPath("tags").description("An array of tag resource URIs"))));
    }


    @Test
    @Order(1)
    public void crudCreateExample() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("equipment_id", 1L);
        crud.put("name", "Sample Model");

        String tagLocation = this.mockMvc.perform(post("/equipment").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        crud.put("tags", singletonList(tagLocation));

        this.mockMvc.perform(post("/equipment").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andDo(document("equipment-create-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), requestFields(fieldWithPath("equipment_id").description("The id of the input"), fieldWithPath("name").description("The title of the input")
                        , fieldWithPath("tags").description("An array of tag resource URIs"))));
    }


    @Test
    @Order(4)
    public void crudDeleteExample() throws Exception {
        this.mockMvc.perform(delete("/equipment/{id}", 2))
                .andExpect(status().isOk())
                .andDo(document("equipment-delete-example", pathParameters(parameterWithName("id").description("The id of the input to delete"))));
    }


    @Test
    @Disabled
    public void crudPatchExample() throws Exception {

        Map<String, String> tag = new HashMap<>();
        tag.put("name", "PATCH");

        String tagLocation = this.mockMvc.perform(patch("/equipment/{id}", 2).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Map<String, Object> crud = new HashMap<>();
        crud.put("name", "Blood");
        crud.put("tags", singletonList(tagLocation));
        this.mockMvc.perform(patch("/equipment/{id}", 2).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk());
    }


    @Test
    @Order(3)
    public void crudPutExample() throws Exception {
        Map<String, Object> tag = new HashMap<>();
        tag.put("equipment_id", 29L);

        tag.put("name", "Blood Pressure");

        String tagLocation = this.mockMvc.perform(put("/equipment/{id}", 2L).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Map<String, Object> crud = new HashMap<>();
        crud.put("equipment_id", 2);
        crud.put("name", "Blood");
        crud.put("tags", singletonList(tagLocation));

        this.mockMvc.perform(put("/equipment/{id}", 2).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk());
    }
}
