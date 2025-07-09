package ecomarket.Ecomarket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ecomarket.Ecomarket.DTO.DetalleCompraDTO;
import ecomarket.Ecomarket.Model.DetalleCompra;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Repartidor;
import ecomarket.Ecomarket.Repositorio.DetalleCompraRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.RepartidorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/detalles-compra")
public class DetalleCompraController {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @PostMapping("/agregar")
    public ResponseEntity<EntityModel<DetalleCompra>> agregarDetalle(@RequestBody DetalleCompraDTO dto) {
        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());
        Optional<Repartidor> repartidorOpt = repartidorRepository.findById(dto.getRepartidorId());

        if (productoOpt.isEmpty() || repartidorOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        DetalleCompra detalle = new DetalleCompra();
        detalle.setProducto(productoOpt.get());
        detalle.setRepartidor(repartidorOpt.get());
        detalle.setFechaCompra(LocalDateTime.now());

        DetalleCompra guardado = detalleCompraRepository.save(detalle);
        return ResponseEntity.ok(toModel(guardado));
    }

    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<DetalleCompra>>> listarDetalles() {
        List<EntityModel<DetalleCompra>> detalles = detalleCompraRepository.findAll().stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(detalles,
            linkTo(methodOn(DetalleCompraController.class).listarDetalles()).withSelfRel()));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarDetalle(@PathVariable Integer id, @RequestBody DetalleCompraDTO dto) {
        Optional<DetalleCompra> detalleOpt = detalleCompraRepository.findById(id);
        if (detalleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado");
        }

        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());
        Optional<Repartidor> repartidorOpt = repartidorRepository.findById(dto.getRepartidorId());

        if (productoOpt.isEmpty() || repartidorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Producto o Repartidor no válido");
        }

        DetalleCompra detalle = detalleOpt.get();
        detalle.setProducto(productoOpt.get());
        detalle.setRepartidor(repartidorOpt.get());

        DetalleCompra actualizado = detalleCompraRepository.save(detalle);
        return ResponseEntity.ok(toModel(actualizado));
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer id) {
        if (!detalleCompraRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado");
        }

        detalleCompraRepository.deleteById(id);
        return ResponseEntity.ok("Detalle eliminado");
    }


    private EntityModel<DetalleCompra> toModel(DetalleCompra detalle) {
        return EntityModel.of(detalle,
            linkTo(methodOn(DetalleCompraController.class).listarDetalles()).withRel("lista-detalles"),
            linkTo(methodOn(DetalleCompraController.class).actualizarDetalle(detalle.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(DetalleCompraController.class).eliminarDetalle(detalle.getId())).withRel("eliminar"));
    }
}