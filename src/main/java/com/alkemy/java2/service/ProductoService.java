package com.alkemy.java2.service;

import com.alkemy.java2.dto.ProductoDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public interface ProductoService {
    ProductoDTO crearProducto(ProductoDTO productoDTO);
    ProductoDTO obtenerPorId(String id);
    List<ProductoDTO> listarProductos();
    void eliminarProducto(String id);
    Optional<ProductoDTO> obtenerPorNombre(String nombre);
    ProductoDTO actualizarProducto(String id, ProductoDTO productoDTO) ;

    CompletableFuture<ProductoDTO> crearProductoAsync(ProductoDTO productoDTO);
    CompletableFuture<ProductoDTO> obtenerPorIdAsync(String id);
    CompletableFuture<List<ProductoDTO>> listarProductosAsync();
    CompletableFuture<Void> eliminarProductoAsync(String id);
    CompletableFuture<Optional<ProductoDTO>> obtenerPorNombreAsync(String nombre);
    CompletableFuture<ProductoDTO> actualizarProductoAsync(String id, ProductoDTO productoDTO);
}
