package com.alkemy.java2.repository;

import com.alkemy.java2.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    // Add this custom exists method
    default boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}
