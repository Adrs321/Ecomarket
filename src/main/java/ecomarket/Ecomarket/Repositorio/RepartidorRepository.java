package ecomarket.Ecomarket.Repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecomarket.Ecomarket.Model.Repartidor;

public interface RepartidorRepository extends JpaRepository<Repartidor, Integer> {
    List<Repartidor> findByProveedorId(Integer proveedorId);
}
