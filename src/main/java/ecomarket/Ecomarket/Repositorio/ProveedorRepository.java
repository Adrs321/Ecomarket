package ecomarket.Ecomarket.Repositorio;

import java.util.List;


import ecomarket.Ecomarket.Model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}