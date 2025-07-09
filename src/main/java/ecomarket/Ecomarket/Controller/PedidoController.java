package ecomarket.Ecomarket.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.DTO.PedidoDTO;
import ecomarket.Ecomarket.DTO.PedidoCreateDTO;
import ecomarket.Ecomarket.DTO.ClienteDTO;
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
    @PostMapping("/crear")
    public ResponseEntity<EntityModel<PedidoDTO>> crearPedido(@RequestBody PedidoCreateDTO pedidoCreateDTO) {
        if (pedidoCreateDTO.getClienteId() == null) {
            throw new RuntimeException("La id del cliente es requerida");
        }
        Cliente cliente = clienteRepository.findById(pedidoCreateDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado(pedidoCreateDTO.getEstado());
        pedido.setDireccionEntrega(pedidoCreateDTO.getDireccionEntrega());
        pedido.setMetodoPago(pedidoCreateDTO.getMetodoPago());
        Pedido savedPedido = pedidoRepository.save(pedido);
        return ResponseEntity.ok(toModelDTO(savedPedido));
    }

    // Listar todos los pedidos
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<PedidoDTO>>> listarTodosLosPedidos() {
        List<EntityModel<PedidoDTO>> pedidos = pedidoRepository.findAll().stream()
                .map(this::toModelDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).listarTodosLosPedidos()).withSelfRel()));
    }

    // Listar un pedido por ID
    @GetMapping("/listar/{id}")
    public ResponseEntity<EntityModel<PedidoDTO>> obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return ResponseEntity.ok(toModelDTO(pedido));
    }

    // Actualizar un pedido
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<PedidoDTO>> actualizarPedido(@PathVariable Long id, @RequestBody PedidoCreateDTO pedidoCreateDTO) {
        Pedido updatedPedido = pedidoRepository.findById(id).map(existingPedido -> {
            existingPedido.setEstado(pedidoCreateDTO.getEstado());
            existingPedido.setDireccionEntrega(pedidoCreateDTO.getDireccionEntrega());
            existingPedido.setMetodoPago(pedidoCreateDTO.getMetodoPago());
            if (pedidoCreateDTO.getClienteId() == null) {
                throw new RuntimeException("La id del cliente es requerida");
            }
            Cliente cliente = clienteRepository.findById(pedidoCreateDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            existingPedido.setCliente(cliente);
            return pedidoRepository.save(existingPedido);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return ResponseEntity.ok(toModelDTO(updatedPedido));
    }

    private EntityModel<PedidoDTO> toModelDTO(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        ClienteDTO clienteDTO = null;
        if (cliente != null) {
            clienteDTO = new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getContrasena(),
                null // No incluir lista de pedidos para evitar recursión
            );
        }
        PedidoDTO pedidoDTO = new PedidoDTO(
                pedido.getId(),
                pedido.getEstado(),
                pedido.getDireccionEntrega(),
                pedido.getMetodoPago(),
                clienteDTO,
                pedido.getFechaPedido(),
                pedido.getFechaEntrega()
        );

        EntityModel<PedidoDTO> pedidoModel = EntityModel.of(pedidoDTO,
                linkTo(methodOn(PedidoController.class).obtenerPedidoPorId(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).actualizarPedido(pedido.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(PedidoController.class).listarTodosLosPedidos()).withRel("pedidos"));

        if (clienteDTO != null) {
            pedidoModel.add(linkTo(methodOn(ClienteController.class).obtenerClientePorId(clienteDTO.getId())).withRel("cliente"));
        }

        return pedidoModel;
    }
}
