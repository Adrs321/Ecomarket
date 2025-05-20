package com.example.EcoMarket.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.EcoMarket.Model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
