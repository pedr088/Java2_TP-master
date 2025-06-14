package com.alkemy.java2.mapper;

import com.alkemy.java2.dto.ProductoDTO;
import com.alkemy.java2.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    ProductoDTO toDTO(Producto producto);
    Producto toEntity(ProductoDTO productoDTO);
}
