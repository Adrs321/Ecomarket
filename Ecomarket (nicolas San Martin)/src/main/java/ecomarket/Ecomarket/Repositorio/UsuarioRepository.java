package ecomarket.Ecomarket.Repositorio;

import ecomarket.Ecomarket.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreoElectronico(String correoElectronico);
    Usuario findByCorreoElectronico(String correoElectronico);
}
