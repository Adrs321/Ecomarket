package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.Model.Cupon;
import ecomarket.Ecomarket.Repositorio.CuponRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    @Autowired
    private CuponRepository cuponRepository;

    // Crear cupón
    @PostMapping("/crear")
    public ResponseEntity<Cupon> crearCupon(@RequestBody Cupon cupon) {
        // Validar unicidad del código
        if (cuponRepository.findByCodigo(cupon.getCodigo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un cupón con ese código");
        }
        return ResponseEntity.ok(cuponRepository.save(cupon));
    }

    // Listar todos los cupones
    @GetMapping("/listar")
    public ResponseEntity<List<Cupon>> listarCupones() {
        return ResponseEntity.ok(cuponRepository.findAll());
    }

    // Obtener un cupón por código
    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<Cupon> buscarCupon(@PathVariable String codigo) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
        return ResponseEntity.ok(cupon);
    }

    // Verificar validez de cupón
    @GetMapping("/verificar/{codigo}")
    public ResponseEntity<Cupon> verificarCupon(@PathVariable String codigo) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
                .filter(c -> Boolean.TRUE.equals(c.getActivo()) &&
                        (c.getFechaExpiracion() == null || !c.getFechaExpiracion().isBefore(LocalDate.now())))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no válido o expirado"));
        return ResponseEntity.ok(cupon);
    }

    // Actualizar cupón
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Cupon> actualizarCupon(@PathVariable Long id, @RequestBody Cupon cuponRequest) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));

        // Solo actualiza campos editables
        cupon.setCodigo(cuponRequest.getCodigo());
        cupon.setDescuento(cuponRequest.getDescuento());
        cupon.setFechaExpiracion(cuponRequest.getFechaExpiracion());
        cupon.setActivo(cuponRequest.getActivo());

        return ResponseEntity.ok(cuponRepository.save(cupon));
    }

    // Desactivar cupón (soft delete)
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<Cupon> desactivarCupon(@PathVariable Long id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
        cupon.setActivo(false);
        return ResponseEntity.ok(cuponRepository.save(cupon));
    }

    // Eliminar cupón (borrado físico)
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCupon(@PathVariable Long id) {
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
        cuponRepository.delete(cupon);
        return ResponseEntity.noContent().build();
    }
}
