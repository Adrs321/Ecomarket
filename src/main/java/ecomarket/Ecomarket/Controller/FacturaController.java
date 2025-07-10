package ecomarket.Ecomarket.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ecomarket.Ecomarket.DTO.FacturaDTO;
import ecomarket.Ecomarket.DTO.PedidoDTO;
import ecomarket.Ecomarket.DTO.ClienteDTO;
import ecomarket.Ecomarket.Model.Factura;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Repositorio.FacturaRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear factura
    @PostMapping("/crear")
    public ResponseEntity<EntityModel<FacturaDTO>> crearFactura(@RequestBody FacturaDTO facturaDTO) {
        Factura factura = fromDTO(facturaDTO);
        Factura savedFactura = facturaRepository.save(factura);
        return ResponseEntity.ok(toModel(savedFactura));
    }

    // Listar facturas
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<FacturaDTO>>> listarFacturas() {
        List<EntityModel<FacturaDTO>> facturas = facturaRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(facturas,
                linkTo(methodOn(FacturaController.class).listarFacturas()).withSelfRel()));
    }

    // Obtener factura por ID
    @GetMapping("/listar/{id}")
    public ResponseEntity<EntityModel<FacturaDTO>> obtenerFacturaPorId(@PathVariable Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return ResponseEntity.ok(toModel(factura));
    }

    // Actualizar factura
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<FacturaDTO>> actualizarFactura(@PathVariable Long id, @RequestBody FacturaDTO facturaDTO) {
        Factura updatedFactura = facturaRepository.findById(id).map(factura -> {
            factura.setFechaEmision(facturaDTO.getFechaEmision());
            factura.setMontoTotal(facturaDTO.getMontoTotal());

            if (facturaDTO.getIdPedido() != null) {
                Pedido pedido = pedidoRepository.findById(facturaDTO.getIdPedido())
                        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + facturaDTO.getIdPedido()));
                factura.setPedido(pedido);
            } else {
                factura.setPedido(null);
            }

            if (facturaDTO.getIdCliente() != null) {
                Cliente cliente = clienteRepository.findById(facturaDTO.getIdCliente())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + facturaDTO.getIdCliente()));
                factura.setCliente(cliente);
            } else {
                factura.setCliente(null);
            }

            return facturaRepository.save(factura);
        }).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return ResponseEntity.ok(toModel(updatedFactura));
    }

    private EntityModel<FacturaDTO> toModel(Factura factura) {
        FacturaDTO dto = toDTO(factura);
        return EntityModel.of(dto,
                linkTo(methodOn(FacturaController.class).obtenerFacturaPorId(factura.getId())).withSelfRel(),
                linkTo(methodOn(FacturaController.class).actualizarFactura(factura.getId(), dto)).withRel("actualizar"),
                linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas"));
    }

    private FacturaDTO toDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setMontoTotal(factura.getMontoTotal());
        if (factura.getPedido() != null) {
            dto.setIdPedido(factura.getPedido().getId());
            PedidoDTO pedidoDTO = new PedidoDTO();
            pedidoDTO.setId(factura.getPedido().getId());
            pedidoDTO.setMetodoPago(factura.getPedido().getMetodoPago());
            pedidoDTO.setEstado(factura.getPedido().getEstado());
            pedidoDTO.setDireccionEntrega(factura.getPedido().getDireccionEntrega());
            pedidoDTO.setFechaPedido(factura.getPedido().getFechaPedido());
            pedidoDTO.setFechaEntrega(factura.getPedido().getFechaEntrega());
            // Asignar cliente en pedidoDTO si existe
            if (factura.getPedido().getCliente() != null) {
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setId(factura.getPedido().getCliente().getId());
                clienteDTO.setNombre(factura.getPedido().getCliente().getNombre());
                clienteDTO.setCorreo(factura.getPedido().getCliente().getCorreo());
                clienteDTO.setDireccion(factura.getPedido().getCliente().getDireccion());
                clienteDTO.setTelefono(factura.getPedido().getCliente().getTelefono());
                pedidoDTO.setCliente(clienteDTO);
            }
            dto.setPedido(pedidoDTO);
        }
        if (factura.getCliente() != null) {
            dto.setIdCliente(factura.getCliente().getId());
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setId(factura.getCliente().getId());
            clienteDTO.setNombre(factura.getCliente().getNombre());
            clienteDTO.setCorreo(factura.getCliente().getCorreo());
            clienteDTO.setDireccion(factura.getCliente().getDireccion());
            clienteDTO.setTelefono(factura.getCliente().getTelefono());
            dto.setCliente(clienteDTO);
        }
        return dto;
    }

    private Factura fromDTO(FacturaDTO dto) {
        Factura factura = new Factura();
        // No asignar ID para evitar problemas con Hibernate
        factura.setFechaEmision(dto.getFechaEmision());
        factura.setMontoTotal(dto.getMontoTotal());

        if (dto.getIdPedido() != null) {
            Pedido pedido = pedidoRepository.findById(dto.getIdPedido())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + dto.getIdPedido()));
            factura.setPedido(pedido);
        }

        if (dto.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + dto.getIdCliente()));
            factura.setCliente(cliente);
        }

        return factura;
    }
}
