package com.example.EcoMarket.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EcoMarket.Model.Categoria;
import com.example.EcoMarket.Model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombre(String nombre);
    Iterable<Producto> findByCategoria(Categoria categoria);
}
