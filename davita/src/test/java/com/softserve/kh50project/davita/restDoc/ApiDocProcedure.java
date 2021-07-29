package com.softserve.kh50project.davita.restDoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.kh50project.davita.DavitaApplication;
import com.softserve.kh50project.davita.model.Equipment;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = DavitaApplication.class)
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
    @Order(2)
    public void crudGetExample() throws Exception {

        Map<String, Object> crud = new HashMap<>();
        crud.put("procedure_id", 30);
        crud.put("name", "Sample Model");
        crud.put("duration", "10:15:30");
        crud.put("cost", 10.20);
        crud.put("equipment_id", 10L);

        String tagLocation = this.mockMvc.perform(get("/procedures/{id}", 21L).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        crud.put("tags", singletonList(tagLocation));

        ConstraintDescriptions desc = new ConstraintDescriptions(Equipment.class);

        this.mockMvc.perform(get("/procedures/{id}", 21L).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("procedures-get-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The title of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input"),
                                fieldWithPath("equipment_id").description("The cost of the input"),
                                fieldWithPath("procedure_id").description("The cost of the input"),
                                fieldWithPath("tags").description("An array of tag resource URIs"))));
    }


    @Test
    public void crudCreateExample() throws Exception {
        Map<String, Object> crud = new HashMap<>();
        crud.put("cost", 10.20);
        crud.put("duration", LocalTime.now());
        crud.put("name", "Blood");

        String tagLocation = this.mockMvc.perform(post("/procedures").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        crud.put("tags", singletonList(tagLocation));


        this.mockMvc.perform(post("/procedures").contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isCreated())
                .andDo(document("procedures-create-example", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The name of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input"),

                                fieldWithPath("tags").description("An array of tag resource URIs"))));
    }

/*

    @Test
    public void crudDeleteExample() throws Exception {
        this.mockMvc.perform(delete("/procedures/{id}", 29))
                .andExpect(status().isOk())
                .andDo(document("procedures-delete-example", pathParameters(parameterWithName("id").description("The id of the input to delete"))));
    }
*/


    @Test
    public void crudPutExample() throws Exception {
        Map<String, String> tag = new HashMap<>();
        tag.put("name", "Sample Model");
        tag.put("duration", "10:15:30");
        tag.put("cost", "10.20");

        String tagLocation = this.mockMvc.perform(put("/procedures/{id}", 60).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(tag)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        Map<String, Object> crud = new HashMap<>();
        crud.put("name", " Model");
        crud.put("duration", "20:15:30");
        crud.put("cost", 20.20);

        this.mockMvc.perform(put("/procedures/{id}", 60).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk());


        this.mockMvc.perform(put("/procedures/{id}",60).contentType(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(crud)))
                .andExpect(status().isOk())
                .andDo(document("procedures-put-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The title of the input"),
                                fieldWithPath("duration").description("The duration of the input"),
                                fieldWithPath("cost").description("The cost of the input")
                               )));
    }
}

