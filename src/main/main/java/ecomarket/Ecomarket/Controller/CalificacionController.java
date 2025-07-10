package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.CalificacionDTO;
import ecomarket.Ecomarket.Model.Calificacion;
import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Repositorio.CalificacionRepository;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/agregar")
    public ResponseEntity<EntityModel<CalificacionDTO>> crearCalificacion(@RequestBody CalificacionDTO dto) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(dto.getClienteId());
        Optional<Producto> productoOpt = productoRepository.findById(dto.getProductoId());

        if (clienteOpt.isEmpty() || productoOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setPuntaje(dto.getPuntaje());
        calificacion.setComentario(dto.getComentario());
        calificacion.setFecha(LocalDateTime.now());
        calificacion.setCliente(clienteOpt.get());
        calificacion.setProducto(productoOpt.get());

        Calificacion guardada = calificacionRepository.save(calificacion);
        return ResponseEntity.ok(toModel(guardada));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Calificacion>> listarCalificaciones() {
        List<Calificacion> calificaciones = calificacionRepository.findAll();
        return ResponseEntity.ok(calificaciones);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<CollectionModel<EntityModel<CalificacionDTO>>> obtenerPorProducto(@PathVariable Integer productoId) {
        List<EntityModel<CalificacionDTO>> calificaciones = calificacionRepository.findByProductoId(productoId).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(calificaciones,
                linkTo(methodOn(CalificacionController.class).obtenerPorProducto(productoId)).withSelfRel()));
    }

    @GetMapping("/promedio/{productoId}")
    public ResponseEntity<Double> obtenerPromedio(@PathVariable Integer productoId) {
        List<Calificacion> calificaciones = calificacionRepository.findByProductoId(productoId);
        double promedio = calificaciones.stream()
                .mapToInt(Calificacion::getPuntaje)
                .average()
                .orElse(0);
        return ResponseEntity.ok(promedio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (!calificacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        calificacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CalificacionDTO>> actualizar(@PathVariable Integer id, @RequestBody CalificacionDTO dto) {
        Optional<Calificacion> calOpt = calificacionRepository.findById(id);
        if (calOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Calificacion calificacion = calOpt.get();
        calificacion.setComentario(dto.getComentario());
        calificacion.setPuntaje(dto.getPuntaje());
        calificacion.setFecha(LocalDateTime.now());

        Calificacion actualizada = calificacionRepository.save(calificacion);
        return ResponseEntity.ok(toModel(actualizada));
    }


    private EntityModel<CalificacionDTO> toModel(Calificacion calificacion) {
        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(calificacion.getId());
        dto.setComentario(calificacion.getComentario());
        dto.setFecha(calificacion.getFecha());
        dto.setPuntaje(calificacion.getPuntaje());
        dto.setClienteId(calificacion.getCliente().getId());
        dto.setProductoId(calificacion.getProducto().getId());

        return EntityModel.of(dto,
                linkTo(methodOn(CalificacionController.class).obtenerPorProducto(dto.getProductoId())).withRel("porProducto"),
                linkTo(methodOn(CalificacionController.class).obtenerPromedio(dto.getProductoId())).withRel("promedioProducto"),
                linkTo(methodOn(CalificacionController.class).actualizar(dto.getId(), dto)).withRel("actualizar"),
                linkTo(methodOn(CalificacionController.class).eliminar(dto.getId())).withRel("eliminar"));
    }
}

