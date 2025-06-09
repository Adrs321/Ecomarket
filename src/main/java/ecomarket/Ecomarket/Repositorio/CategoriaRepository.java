package ecomarket.Ecomarket.Repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecomarket.Ecomarket.Model.Categoria;



public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}

