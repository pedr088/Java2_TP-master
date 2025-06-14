package com.alkemy.java2.mapper;

import com.alkemy.java2.authsecurity.dto.RegisterRequest;
import com.alkemy.java2.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Usuario toUsuario(RegisterRequest registerRequest);


}
