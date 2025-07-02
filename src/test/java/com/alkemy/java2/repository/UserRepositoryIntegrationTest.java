package com.alkemy.java2.repository;

import com.alkemy.java2.model.Usuario;
import com.alkemy.java2.model.Rol;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
class UserRepositoryIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        testUsuario = Usuario.builder()
                .username("testUser")
                .password("password")
                .activo(true)
                .rol(Set.of(Rol.USER))
                .build();
        usuarioRepository.save(testUsuario);
    }

    @Test
    void findByUsername_ShouldReturnUsuario_WhenExists() {
        Optional<Usuario> foundUsuario = usuarioRepository.findByUsername("testUser");
        assertThat(foundUsuario).isPresent();
        assertEquals(testUsuario.getUsername(), foundUsuario.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotExists() {
        Optional<Usuario> foundUsuario = usuarioRepository.findByUsername("nonExistentUser");
        assertThat(foundUsuario).isEmpty();
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenExists() {
        boolean exists = usuarioRepository.existsByUsername("testUser");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenNotExists() {
        boolean exists = usuarioRepository.existsByUsername("nonExistentUser");
        assertThat(exists).isFalse();
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Arrange
        Usuario anotherUser = Usuario.builder()
                .username("anotheruser")
                .password("anotherpass")
                .activo(true)
                .rol(Set.of(Rol.USER))
                .build();
        usuarioRepository.save(anotherUser);

        // Act
        List<Usuario> users = usuarioRepository.findAll();

        // Assert
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser")));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("anotheruser")));
    }

    @Test
    void update_shouldModifyExistingUser() {
        // Arrange
        testUsuario.setPassword("newpassword");

        // Act
        Usuario updatedUser = usuarioRepository.save(testUsuario);

        // Assert
        assertEquals(testUsuario.getUsername(), updatedUser.getUsername());
        assertEquals("newpassword", updatedUser.getPassword());

        Optional<Usuario> retrievedUser = usuarioRepository.findByUsername(testUsuario.getUsername());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newpassword", retrievedUser.get().getPassword());
    }

    @Test
    void count_shouldReturnNumberOfUsers() {
        // Arrange
        Usuario anotherUser = Usuario.builder()
                .username("anotheruser")
                .password("anotherpass")
                .activo(true)
                .rol(Set.of(Rol.USER))
                .build();
        usuarioRepository.save(anotherUser);

        // Act
        long count = usuarioRepository.count();

        // Assert
        assertEquals(2, count);
    }
}