package com.alkemy.java2.authsecurity.service.impl;

import com.alkemy.java2.authsecurity.dto.AuthRequest;
import com.alkemy.java2.authsecurity.dto.AuthResponse;
import com.alkemy.java2.authsecurity.dto.RegisterRequest;
import com.alkemy.java2.authsecurity.service.AuthService;
import com.alkemy.java2.authsecurity.service.JwtService;
import com.alkemy.java2.exception.UserAlreadyExistsException;
import com.alkemy.java2.mapper.UsuarioMapper;
import com.alkemy.java2.model.Usuario;
import com.alkemy.java2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Lombok annotation for logger
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.debug("Intentando registrar nuevo usuario: {}", request.getUsername());


        // Changed to use our custom exists method
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            log.warn("Username {} already exists", request.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }
        Usuario usuario = usuarioMapper.toUsuario(request);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());

        Usuario savedUser = usuarioRepository.save(usuario);
        log.info("Nuevo usuario registrado con ID: {}", savedUser.getId());

        String jwtToken = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        log.debug("Autenticando usuario: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        log.error("Usuario no encontrado después de autenticación exitosa: {}", request.getUsername());
                        return new UsernameNotFoundException("User not found");
                    });

            String jwtToken = jwtService.generateToken(usuario);
            log.info("Usuario {} autenticado exitosamente", usuario.getUsername());
            usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .forEach(authority -> log.info("User authority: {}", authority));

            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (AuthenticationException e) {
            log.warn("Falló autenticación para usuario: {}", request.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}