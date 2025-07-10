package ecomarket.Ecomarket.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import ecomarket.Ecomarket.Model.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByCorreo(String correo);
}
