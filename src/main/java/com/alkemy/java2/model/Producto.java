package com.alkemy.java2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="productos")
public class Producto {
    @Id
    private String id;

    private String nombre;
    private double precio;
    private boolean enStock;
    private Set<Color> colores;

}