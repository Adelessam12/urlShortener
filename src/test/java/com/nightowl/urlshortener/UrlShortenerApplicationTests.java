package com.nightowl.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nightowl.urlshortener.Dtos.UrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateShortUrlAndFetchIt() throws Exception {
        // 1. Prepare the request
        UrlRequest request = new UrlRequest();
        request.setUrl("https://www.google.com");

        // 2. Post the URL to create a short code
        MvcResult result = mockMvc.perform(post("/api/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").exists())
                .andExpect(jsonPath("$.url").value("https://www.google.com"))
                .andReturn();

        // Extract the generated short code from the response
        String responseContent = result.getResponse().getContentAsString();
        String shortCode = JsonPath.read(responseContent, "$.shortCode");

        // 3. Fetch the original URL using the generated short code
        mockMvc.perform(get("/api/urls/" + shortCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://www.google.com"))
                .andExpect(jsonPath("$.shortCode").value(shortCode));
    }
}
