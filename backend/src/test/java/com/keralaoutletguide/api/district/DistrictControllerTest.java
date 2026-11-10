package com.keralaoutletguide.api.district;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DistrictControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void returnsAllDistrictsInDisplayOrderWithStableIds() throws Exception {
        mvc.perform(get("/api/v1/districts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.districts[*].id", contains(
                        "alappuzha", "ernakulam", "idukki", "kannur", "kasaragod", "kollam",
                        "kottayam", "kozhikode", "malappuram", "palakkad", "pathanamthitta",
                        "thiruvananthapuram", "thrissur", "wayanad")))
                .andExpect(jsonPath("$.districts[1].name").value("Ernakulam"));
    }

    @Test
    void returnsJsonNotFoundForAnUnknownRoute() throws Exception {
        mvc.perform(get("/api/v1/districts/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void rejectsPostAndAdvertisesGet() throws Exception {
        mvc.perform(post("/api/v1/districts"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string(HttpHeaders.ALLOW, "GET"))
                .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"));
    }
}
