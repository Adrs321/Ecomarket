package com.example.EcoMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EcoMarket.Model.Categoria;
import com.example.EcoMarket.Model.Producto;
import com.example.EcoMarket.repositorio.CategoriaRepository;
import com.example.EcoMarket.repositorio.ProductoRepository;



@RestController
@RequestMapping("/api/producto")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Crear producto
    @PostMapping("/grabar/{id}")
    public Producto postSave(@RequestBody Producto producto, @PathVariable Long id) {
        Categoria cate= categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        producto.setCategoria(cate);
        return productoRepository.save(producto);        
    }
    // Listar productos
    @GetMapping("/listar")
    public Iterable<Producto> getAll() {
        return productoRepository.findAll();
    }
    // Actualizar producto
    @PutMapping("/actualizar/{idProducto}")
    public Producto putUpdate(@RequestBody Producto producto, @PathVariable Long idProducto) {
        Producto existente = productoRepository.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("El producto con ID " + idProducto + " no existe."));
        producto.setId(idProducto);
        producto.setCategoria(existente.getCategoria());
        return productoRepository.save(producto);
    }
    // Eliminar producto
    @DeleteMapping("/eliminar/{id}")
    public void delete(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("El producto con ID " + id + " no existe.");
        }
    }
    // Buscar producto por Nombre
    @GetMapping("/buscarPorNombre/{nombre}")
    public Producto getByNombre(@PathVariable String nombre) {
        return productoRepository.findByNombre(nombre).orElse(null);
    }
    // Buscar producto por Categoria
    @GetMapping("/buscarPorNombreCategoria/{nombre}")
    public Iterable<Producto> getByNombreCategoria(@PathVariable String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return productoRepository.findByCategoria(categoria);
    }
}
