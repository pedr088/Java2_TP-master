package com.alkemy.java2.repository;

import com.alkemy.java2.model.Color;
import com.alkemy.java2.model.Producto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
class ProductoRepositoryIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProductoRepository productoRepository;

    private Producto testProducto;

    @BeforeEach
    void setUp() {
        productoRepository.deleteAll();
        testProducto = new Producto(
                null,
                "Camiseta",
                19.99,
                true,
                Set.of(Color.ROJO, Color.BLANCO)
        );
        testProducto = productoRepository.save(testProducto);
    }

    @Test
    void findByNombre_ShouldReturnProducto_WhenExists() {
        Optional<Producto> found = productoRepository.findByNombre("Camiseta");
        assertThat(found).isPresent();
        assertEquals(testProducto.getNombre(), found.get().getNombre());
    }

    @Test
    void findByNombre_ShouldReturnEmpty_WhenNotExists() {
        Optional<Producto> found = productoRepository.findByNombre("NoExiste");
        assertThat(found).isEmpty();
    }

    @Test
    void findById_ShouldReturnProducto_WhenExists() {
        Optional<Producto> found = productoRepository.findById(testProducto.getId());
        assertThat(found).isPresent();
        assertEquals(testProducto.getNombre(), found.get().getNombre());
    }

    @Test
    void findAll_ShouldReturnAllProductos() {
        Producto otro = new Producto(
                null,
                "Pantalón",
                29.99,
                false,
                Set.of(Color.NEGRO)
        );
        productoRepository.save(otro);

        var productos = productoRepository.findAll();
        assertEquals(2, productos.size());
        assertThat(productos).extracting(Producto::getNombre)
                .contains("Camiseta", "Pantalón");
    }

    @Test
    void count_ShouldReturnNumberOfProductos() {
        Producto otro = new Producto(
                null,
                "Zapatos",
                49.99,
                true,
                Set.of(Color.BLANCO)
        );
        productoRepository.save(otro);

        long count = productoRepository.count();
        assertEquals(2, count);
    }
}