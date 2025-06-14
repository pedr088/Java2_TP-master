package com.alkemy.java2.configuration;

import com.alkemy.java2.model.Rol;
import com.alkemy.java2.model.Usuario;
import com.alkemy.java2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            try {
                // Verificar si la colección de usuarios está vacía
                if (usuarioRepository.count() == 0) {
                    log.info("Inicializando datos de usuarios en la base de datos...");

                    // Usuario admin
                    Usuario admin = createUser("admin@example.com", "Admin123!", true, Set.of(Rol.ADMIN));
                    // Usuario normal
                    Usuario user = createUser("user@example.com", "User123!", true, Set.of(Rol.USER));

                    usuarioRepository.save(admin);
                    usuarioRepository.save(user);

                    log.info("✅ Usuarios iniciales creados exitosamente");
                    log.info("🔑 Credenciales de administrador: admin@example.com / Admin123!");
                    log.info("🔑 Credenciales de usuario normal: user@example.com / User123!");
                } else {
                    log.info("La base de datos ya contiene usuarios, omitiendo inicialización");
                }
            } catch (Exception e) {
                log.error("❌ Error al inicializar datos de usuarios", e);
            }
        };
    }

    private Usuario createUser(String username, String password, boolean activo , Set<Rol> rol) {
        return Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .activo(true)
                .rol(rol)
                .build();
    }
}