package ecomarket.Ecomarket.Repositorio;


import ecomarket.Ecomarket.Model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    List<Calificacion> findByProductoId(Integer productoId);
    
}