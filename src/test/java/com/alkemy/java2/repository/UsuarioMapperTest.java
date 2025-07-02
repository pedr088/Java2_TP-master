package com.alkemy.java2.mapper;

import com.alkemy.java2.authsecurity.dto.RegisterRequest;
import com.alkemy.java2.model.Rol;
import com.alkemy.java2.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    void toUsuario_shouldMapRegisterRequestToUsuario() {
        RegisterRequest request = RegisterRequest.builder()
                .username("test@correo.com")
                .password("Password123!")
                .rol(Set.of(Rol.USER))
                .build();

        Usuario usuario = mapper.toUsuario(request);

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isNull(); // Se ignora en el mapping
        assertThat(usuario.getUsername()).isEqualTo("test@correo.com");
        assertThat(usuario.getPassword()).isEqualTo("Password123!");
        assertThat(usuario.getRol()).containsExactly(Rol.USER);
        assertThat(usuario.isActivo()).isFalse(); // Se ignora y queda en valor por defecto
    }
}
