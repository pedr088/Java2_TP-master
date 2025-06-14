package com.alkemy.java2.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alkemy.java2.dto.ProductoDTO;
import com.alkemy.java2.mapper.ProductoMapper;
import com.alkemy.java2.model.Producto;
import com.alkemy.java2.repository.ProductoRepository;
import com.alkemy.java2.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * CRUD de Producto.
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final ExecutorService executorService;

    @Override
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Producto producto = productoMapper.toEntity(productoDTO);
        log.info("Creando un producto: {}", producto);
        Producto productoGuardado = productoRepository.save(producto);
        return productoMapper.toDTO(productoGuardado);
    }

    @Override
    public CompletableFuture<ProductoDTO> crearProductoAsync(ProductoDTO productoDTO) {
        return CompletableFuture.supplyAsync(() -> crearProducto(productoDTO), executorService);
    }

    @Override
    public ProductoDTO obtenerPorId(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto no existe"));
        return productoMapper.toDTO(producto);
    }

    @Override
    public CompletableFuture<ProductoDTO> obtenerPorIdAsync(String id) {
        return CompletableFuture.supplyAsync(() -> obtenerPorId(id), executorService);
    }

    @Override
    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompletableFuture<List<ProductoDTO>> listarProductosAsync() {
        return CompletableFuture.supplyAsync(this::listarProductos, executorService);
    }

    @Override
    public void eliminarProducto(String id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public CompletableFuture<Void> eliminarProductoAsync(String id) {
        return CompletableFuture.runAsync(() -> eliminarProducto(id), executorService);
    }

    @Override
    public Optional<ProductoDTO> obtenerPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .map(productoMapper::toDTO);
    }

    @Override
    public CompletableFuture<Optional<ProductoDTO>> obtenerPorNombreAsync(String nombre) {
        return CompletableFuture.supplyAsync(() -> obtenerPorNombre(nombre), executorService);
    }

    @Override
    public ProductoDTO actualizarProducto(String id, ProductoDTO productoDTO) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        existente.setNombre(productoDTO.getNombre());
        existente.setPrecio(productoDTO.getPrecio());
        existente.setColores(productoDTO.getColores());
        existente.setEnStock(productoDTO.isEnStock());

        Producto actualizado = productoRepository.save(existente);
        return productoMapper.toDTO(actualizado);
    }

    @Override
    public CompletableFuture<ProductoDTO> actualizarProductoAsync(String id, ProductoDTO productoDTO) {
        return CompletableFuture.supplyAsync(() -> actualizarProducto(id, productoDTO), executorService);
    }

}

