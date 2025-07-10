package ecomarket.Ecomarket.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import ecomarket.Ecomarket.Model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}