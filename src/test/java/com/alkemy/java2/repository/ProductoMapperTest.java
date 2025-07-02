package com.alkemy.java2.repository;

import com.alkemy.java2.dto.ProductoDTO;
import com.alkemy.java2.mapper.ProductoMapper;
import com.alkemy.java2.model.Color;
import com.alkemy.java2.model.Producto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoMapperTest {

    private final ProductoMapper mapper = ProductoMapper.INSTANCE;

    @Test
    void toDTO_shouldMapProductoToProductoDTO() {
        Producto producto = new Producto(
                "123",
                "Zapatillas",
                59.99,
                true,
                Set.of(Color.ROJO, Color.BLANCO)
        );

        ProductoDTO dto = mapper.toDTO(producto);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("123");
        assertThat(dto.getNombre()).isEqualTo("Zapatillas");
        assertThat(dto.getPrecio()).isEqualTo(59.99);
        assertThat(dto.isEnStock()).isTrue();
        assertThat(dto.getColores()).containsExactlyInAnyOrder(Color.ROJO, Color.BLANCO);
    }

    @Test
    void toEntity_shouldMapProductoDTOToProducto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setId("456");
        dto.setNombre("Remera");
        dto.setPrecio(19.99);
        dto.setEnStock(false);
        dto.setColores(Set.of(Color.NEGRO));

        Producto producto = mapper.toEntity(dto);

        assertThat(producto).isNotNull();
        assertThat(producto.getId()).isEqualTo("456");
        assertThat(producto.getNombre()).isEqualTo("Remera");
        assertThat(producto.getPrecio()).isEqualTo(19.99);
        assertThat(producto.isEnStock()).isFalse();
        assertThat(producto.getColores()).containsExactly(Color.NEGRO);
    }
}
