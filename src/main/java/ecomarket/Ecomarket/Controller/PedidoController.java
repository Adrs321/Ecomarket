package ecomarket.Ecomarket.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;



@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    // Crear un nuevo pedido
    @PostMapping("/crear/{id}")
    public Pedido crearPedido(@RequestBody Pedido pedido, @PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado("En proceso");
        return pedidoRepository.save(pedido);
    }
    // Listar todos los pedidos
    @GetMapping("/listar")
    public Iterable<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAll();
    }
    // Listar un pedido por ID
    @GetMapping("/listar/{id}")
    public Pedido obtenerPedidoPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    // Actualizar un pedido
    @PutMapping("/actualizar/{id}")
    public Pedido actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedidoActualizado) {  
    return pedidoRepository.findById(id).map(pedido -> {
        pedido.setEstado(pedidoActualizado.getEstado());
        pedido.setFechaEntrega(pedidoActualizado.getFechaEntrega());
        return pedidoRepository.save(pedido);
    }).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
}
