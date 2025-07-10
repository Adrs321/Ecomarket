package ecomarket.Ecomarket.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.DTO.RepartidorDTO;
import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Model.Repartidor;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import ecomarket.Ecomarket.Repositorio.RepartidorRepository;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorController {

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @PostMapping("/agregar")
    public ResponseEntity<EntityModel<Repartidor>> agregar(@RequestBody RepartidorDTO dto) {
        if (dto.getIdProveedor() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(dto.getIdProveedor());
        if (proveedorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Repartidor repartidor = new Repartidor();
        repartidor.setNombre(dto.getNombre());
        repartidor.setTelefono(dto.getTelefono());
        repartidor.setVehiculo(dto.getVehiculo());
        repartidor.setPatente(dto.getPatenteVehiculo());
        repartidor.setTipoVehiculo(dto.getTipoVehiculo());
        repartidor.setProveedor(proveedorOpt.get());

        Repartidor savedRepartidor = repartidorRepository.save(repartidor);
        return ResponseEntity.ok(toModel(savedRepartidor));
    }

    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<Repartidor>>> listarRepartidores() {
        List<EntityModel<Repartidor>> repartidores = repartidorRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(repartidores,
                linkTo(methodOn(RepartidorController.class).listarRepartidores()).withSelfRel()));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarRepartidor(@PathVariable Integer id) {
        repartidorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<Repartidor>> actualizarRepartidor(@PathVariable Integer id, @RequestBody RepartidorDTO dto) {
        Optional<Repartidor> optional = repartidorRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Repartidor repartidor = optional.get();
        repartidor.setNombre(dto.getNombre());
        repartidor.setTelefono(dto.getTelefono());
        repartidor.setVehiculo(dto.getVehiculo());
        repartidor.setPatente(dto.getPatenteVehiculo());
        repartidor.setTipoVehiculo(dto.getTipoVehiculo());

        if (dto.getIdProveedor() != null) {
            Optional<Proveedor> proveedorOpt = proveedorRepository.findById(dto.getIdProveedor());
            proveedorOpt.ifPresent(repartidor::setProveedor);
        }

        Repartidor updatedRepartidor = repartidorRepository.save(repartidor);
        return ResponseEntity.ok(toModel(updatedRepartidor));
    }

    private EntityModel<Repartidor> toModel(Repartidor repartidor) {
        return EntityModel.of(repartidor,
                linkTo(methodOn(RepartidorController.class).listarRepartidores()).withSelfRel(),
                linkTo(methodOn(RepartidorController.class).agregar(null)).withRel("agregar"),
                linkTo(methodOn(RepartidorController.class).actualizarRepartidor(repartidor.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(RepartidorController.class).eliminarRepartidor(repartidor.getId())).withRel("eliminar"));
    }
}
