package com.alkemy.java2.repository;

import com.alkemy.java2.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends MongoRepository<Producto,String> {
    Optional<Producto>  findByNombre(String nombre);
}
