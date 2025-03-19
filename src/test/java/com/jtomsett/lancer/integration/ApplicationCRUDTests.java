package com.jtomsett.lancer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jtomsett.lancer.entities.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationCRUDTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void afterEach() throws DataAccessException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "application");
    }

    @Test
    void addApplication_simpleValid() throws Exception {
        Application application = new Application();
        ObjectMapper mapper = buildObjectMapper();

        String applicationJsonString = mapper.writer().writeValueAsString(application);

        this.mockMvc.perform(
                post("/application/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationJsonString)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void addApplication_empty() throws Exception {

        String empty = "";
        this.mockMvc.perform(
                        post("/application/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(empty)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getApplicationById_valid() throws Exception {
        Application application = new Application();
        ObjectMapper mapper = buildObjectMapper();

        String applicationJsonString = mapper.writer().writeValueAsString(application);

        MvcResult result = this.mockMvc.perform(
                        post("/application/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(applicationJsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        Application savedApplication = mapper.readValue(result.getResponse().getContentAsString(), Application.class);
        Long appId = savedApplication.getId();

        this.mockMvc.perform(
                get("/application/"+appId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appId));
    }

    @Test
    void getApplicationById_doesNotExist() throws Exception {
        this.mockMvc.perform(
                get("/application/"+3)
        ).andExpect(status().isNotFound());
    }

    @Test
    void deleteApplicationById_valid() throws Exception {
        Application application = new Application();
        ObjectMapper mapper = buildObjectMapper();

        String applicationJsonString = mapper.writer().writeValueAsString(application);

        MvcResult result = this.mockMvc.perform(
                        post("/application/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(applicationJsonString)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();

        Application savedApplication = mapper.readValue(result.getResponse().getContentAsString(), Application.class);
        Long appId = savedApplication.getId();

        this.mockMvc.perform(
                        delete("/application/"+appId)
                )
                .andExpect(status().isOk());
    }


    private ObjectMapper buildObjectMapper(){
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

}
