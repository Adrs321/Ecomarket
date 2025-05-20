package com.example.EcoMarket.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.EcoMarket.Model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
