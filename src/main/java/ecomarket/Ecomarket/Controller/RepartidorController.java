package ecomarket.Ecomarket.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> agregar(@RequestBody RepartidorDTO dto) {
    if (dto.getIdProveedor() == null) {
        return ResponseEntity.badRequest().body("idProveedor no puede ser null");
    }

    Optional<Proveedor> proveedorOpt = proveedorRepository.findById(dto.getIdProveedor());
    if (proveedorOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Proveedor no encontrado");
    }

    Repartidor repartidor = new Repartidor();
    repartidor.setNombre(dto.getNombre());
    repartidor.setTelefono(dto.getTelefono());
    repartidor.setVehiculo(dto.getTipoVehiculo());
    repartidor.setPatente(dto.getPatenteVehiculo());
    repartidor.setTipoVehiculo(dto.getTipoVehiculo());
    repartidor.setProveedor(proveedorOpt.get());

    repartidorRepository.save(repartidor);
    return ResponseEntity.ok("Repartidor agregado correctamente");


}
    @GetMapping("/listar")
    public List<Repartidor> listarRepartidores() {
        return repartidorRepository.findAll();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarRepartidor(@PathVariable Integer id) {
        repartidorRepository.deleteById(id);
        return ResponseEntity.ok("Repartidor eliminado");
    }    

@PutMapping("/actualizar/{id}")
public ResponseEntity<?> actualizarRepartidor(@PathVariable Integer id, @RequestBody RepartidorDTO dto) {
    Optional<Repartidor> optional = repartidorRepository.findById(id);
    if (optional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repartidor no encontrado");
    }

    Repartidor repartidor = optional.get();
    repartidor.setNombre(dto.getNombre());
    repartidor.setTelefono(dto.getTelefono());
    repartidor.setVehiculo(dto.getTipoVehiculo());
    repartidor.setPatente(dto.getPatenteVehiculo());
    repartidor.setTipoVehiculo(dto.getTipoVehiculo());

    if (dto.getIdProveedor() != null) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(dto.getIdProveedor());
        proveedorOpt.ifPresent(repartidor::setProveedor);
    }

    repartidorRepository.save(repartidor);
    return ResponseEntity.ok("Repartidor actualizado con éxito");
}
}
