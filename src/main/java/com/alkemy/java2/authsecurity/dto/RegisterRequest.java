package com.alkemy.java2.authsecurity.dto;

import com.alkemy.java2.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RegisterRequest {
    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    private Set<Rol> rol;
}