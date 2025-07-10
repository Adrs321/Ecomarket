package ecomarket.Ecomarket.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Model.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByProveedorId(Integer idProveedor);
    Optional<Producto> findByNombre(String nombre);
    Iterable<Producto> findByCategoria(Categoria categoria);

}