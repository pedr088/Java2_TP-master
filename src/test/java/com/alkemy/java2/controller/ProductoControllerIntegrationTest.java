package com.alkemy.java2.controller;

import com.alkemy.java2.dto.ProductoDTO;
import com.alkemy.java2.model.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductoControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setNombre("Camiseta");
        productoDTO.setPrecio(19.99);
        productoDTO.setEnStock(true);
        productoDTO.setColores(Set.of(Color.ROJO, Color.BLANCO));
    }

    @Test
    void crearYObtenerProductoPorId() throws Exception {
        // Crear producto
        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Camiseta"))
                .andReturn().getResponse().getContentAsString();

        ProductoDTO creado = objectMapper.readValue(response, ProductoDTO.class);

        // Obtener por id
        mockMvc.perform(get("/api/productos/" + creado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Camiseta"));
    }

    @Test
    void listarProductos() throws Exception {
        // Crear producto
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated());

        // Listar productos
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Camiseta"));
    }

    @Test
    void obtenerPorNombre() throws Exception {
        // Crear producto
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated());

        // Buscar por nombre
        mockMvc.perform(get("/api/productos/nombre/Camiseta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Camiseta"));
    }

    @Test
    void actualizarProducto() throws Exception {
        // Crear producto
        String response = mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ProductoDTO creado = objectMapper.readValue(response, ProductoDTO.class);

        // Actualizar producto
        creado.setNombre("Remera");
        creado.setPrecio(29.99);

        mockMvc.perform(put("/api/productos/" + creado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Remera"))
                .andExpect(jsonPath("$.precio").value(29.99));
    }

    @Test
    void eliminarProducto() throws Exception {
        // Crear producto
        String response = mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ProductoDTO creado = objectMapper.readValue(response, ProductoDTO.class);

        // Eliminar producto
        mockMvc.perform(delete("/api/productos/" + creado.getId()))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/api/productos/" + creado.getId()))
                .andExpect(status().isInternalServerError());
    }
}