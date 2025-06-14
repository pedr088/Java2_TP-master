package com.alkemy.java2.authsecurity.service;


import com.alkemy.java2.authsecurity.dto.AuthRequest;
import com.alkemy.java2.authsecurity.dto.AuthResponse;
import com.alkemy.java2.authsecurity.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}