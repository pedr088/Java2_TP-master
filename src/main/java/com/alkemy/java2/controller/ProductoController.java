package com.alkemy.java2.controller;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.alkemy.java2.dto.ProductoDTO;
import com.alkemy.java2.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Service para CRUD de producto en MongoDB.
 * */

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ProductoDTO>>> listarTodos() {
        return productoService.listarProductosAsync()
                .thenApply(ResponseEntity::ok);

    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ProductoDTO>> crearProductoAsync(@RequestBody ProductoDTO productoDTO) {
        return productoService.crearProductoAsync(productoDTO)
                .thenApply(producto -> new ResponseEntity<>(producto, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductoDTO>> obtenerPorId(@PathVariable String id) {
        return productoService.obtenerPorIdAsync(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/nombre/{name}")
    public CompletableFuture<ResponseEntity<ProductoDTO>> obtenerPorNombre(@PathVariable String name) {
        return productoService.obtenerPorNombreAsync(name)
                .thenApply(productoOpt -> productoOpt
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> eliminarProducto(@PathVariable String id) {
        return productoService.eliminarProductoAsync(id)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductoDTO>> actualizarProducto(
            @PathVariable String id, @RequestBody ProductoDTO productoDTO) {
        return productoService.actualizarProductoAsync(id, productoDTO)
                .thenApply(ResponseEntity::ok);
    }

}