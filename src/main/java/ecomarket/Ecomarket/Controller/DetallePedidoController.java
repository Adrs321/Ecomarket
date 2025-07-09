package ecomarket.Ecomarket.Controller;

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

import ecomarket.Ecomarket.Model.DetallePedido;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.DTO.DetallePedidoDTO;
import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.DTO.ProveedorDTO;
import ecomarket.Ecomarket.Repositorio.DetallePedidoRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;

@RestController
@RequestMapping("/api/detallePedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    ////Crear un detalle de pedido
    @PostMapping("/grabar")
    public ResponseEntity<EntityModel<DetallePedidoDTO>> crearDetallePedido(@RequestBody DetallePedidoDTO detallePedidoDTO) {
        if (detallePedidoDTO.getProducto() == null || detallePedidoDTO.getProducto().getId() == null) {
            throw new RuntimeException("Debe indicar el id del producto");
        }
        Producto producto = productoRepository.findById(detallePedidoDTO.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setCantidad(detallePedidoDTO.getCantidad());
        detallePedido.setPrecioUnitario(detallePedidoDTO.getPrecioUnitario().doubleValue());
        detallePedido.setProducto(producto);

        DetallePedido savedDetallePedido = detallePedidoRepository.save(detallePedido);
        return ResponseEntity.ok(toModelDTO(savedDetallePedido));
    }

    ////Listar todos los detalles de pedidos
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<DetallePedidoDTO>>> listarDetallesPedidos() {
        List<EntityModel<DetallePedidoDTO>> detallesPedidos = detallePedidoRepository.findAll().stream()
                .map(this::toModelDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(detallesPedidos,
                linkTo(methodOn(DetallePedidoController.class).listarDetallesPedidos()).withSelfRel()));
    }

    // Actualizar un detalle de pedido
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<DetallePedidoDTO>> actualizarDetallePedido(@PathVariable Long id, @RequestBody DetallePedido detallePedidoActualizado) {
        DetallePedido updatedDetallePedido = detallePedidoRepository.findById(id).map(detallePedido -> {
            detallePedido.setCantidad(detallePedidoActualizado.getCantidad());
            detallePedido.setPrecioUnitario(detallePedidoActualizado.getPrecioUnitario());
            detallePedido.setProducto(detallePedidoActualizado.getProducto());
            return detallePedidoRepository.save(detallePedido);
        }).orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
        return ResponseEntity.ok(toModelDTO(updatedDetallePedido));
    }

    private EntityModel<DetallePedidoDTO> toModelDTO(DetallePedido detallePedido) {
        Producto producto = detallePedido.getProducto();
        ProductoDTO productoDTO = null;
        if (producto != null) {
            productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setStock(producto.getStock());

            if (producto.getProveedor() != null) {
                ProveedorDTO proveedorDTO = new ProveedorDTO();
                proveedorDTO.setIdProveedor(producto.getProveedor().getId());
                proveedorDTO.setNombre(producto.getProveedor().getNombre());
                proveedorDTO.setDireccion(producto.getProveedor().getDireccion());
                proveedorDTO.setTelefono(producto.getProveedor().getTelefono());
                productoDTO.setProveedorId(proveedorDTO.getIdProveedor());
            }
        }

        DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO(
                detallePedido.getId(),
                detallePedido.getCantidad(),
                detallePedido.getPrecioUnitario(),
                productoDTO
        );

        EntityModel<DetallePedidoDTO> detallePedidoModel = EntityModel.of(detallePedidoDTO,
                linkTo(methodOn(DetallePedidoController.class).listarDetallesPedidos()).withRel("detallesPedidos"),
                linkTo(methodOn(DetallePedidoController.class).crearDetallePedido(null)).withRel("crear"),
                linkTo(methodOn(DetallePedidoController.class).actualizarDetallePedido(detallePedido.getId(), detallePedido)).withRel("actualizar"),
                linkTo(methodOn(DetallePedidoController.class).listarDetallesPedidos()).withSelfRel());

        return detallePedidoModel;
    }
}

