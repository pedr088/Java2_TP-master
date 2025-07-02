package com.alkemy.java2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void mostrarHola_shouldReturnHelloMessage() throws Exception {
        mockMvc.perform(get("/api/v1/test/hola"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola desplegado desde controller test"));
    }
}