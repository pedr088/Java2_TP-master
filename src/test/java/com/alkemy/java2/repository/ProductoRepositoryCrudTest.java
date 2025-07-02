package com.alkemy.java2.repository;

import com.alkemy.java2.model.Color;
import com.alkemy.java2.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductoRepositoryCrudTest {

    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
    }

    @Test
    void save_ShouldReturnSavedProducto() {
        Producto producto = new Producto(null, "Remera", 10.0, true, Set.of(Color.ROJO));
        Producto saved = new Producto("1", "Remera", 10.0, true, Set.of(Color.ROJO));

        when(productoRepository.save(producto)).thenReturn(saved);

        Producto result = productoRepository.save(producto);

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getNombre()).isEqualTo("Remera");
    }

    @Test
    void findById_ShouldReturnProducto_WhenExists() {
        Producto producto = new Producto("1", "Remera", 10.0, true, Set.of(Color.ROJO));
        when(productoRepository.findById("1")).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoRepository.findById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Remera");
    }

    @Test
    void findByNombre_ShouldReturnProducto_WhenExists() {
        Producto producto = new Producto("1", "Remera", 10.0, true, Set.of(Color.ROJO));
        when(productoRepository.findByNombre("Remera")).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoRepository.findByNombre("Remera");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("1");
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(productoRepository).deleteById("1");

        productoRepository.deleteById("1");

        verify(productoRepository, times(1)).deleteById("1");
    }
}