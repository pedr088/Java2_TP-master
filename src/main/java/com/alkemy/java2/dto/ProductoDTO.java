package com.alkemy.java2.dto;

import com.alkemy.java2.model.Color;
import lombok.Data;

import java.util.Set;

@Data
public class ProductoDTO {
    private String id;
    private String nombre;
    private double precio;
    private boolean enStock;
    private Set<Color> colores;
}