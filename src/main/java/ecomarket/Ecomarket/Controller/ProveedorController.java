package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.DTO.ProveedorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/agregar")
    public ResponseEntity<Proveedor> crearProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(proveedorDTO.getNombre());
        proveedor.setCorreo(proveedorDTO.getCorreo());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setTelefono(proveedorDTO.getTelefono());

        if (proveedorDTO.getProductoIds() != null) {
            List<Producto> productos = productoRepository.findAllById(proveedorDTO.getProductoIds());
            productos.forEach(p -> p.setProveedor(proveedor));
            proveedor.setProductos(productos);
        }

        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return ResponseEntity.ok(proveedorGuardado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProveedor(@PathVariable Integer id) {
        proveedorRepository.deleteById(id);
        return ResponseEntity.ok("Proveedor eliminado");
    }

    @GetMapping("/listar")
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Integer id, @RequestBody ProveedorDTO proveedorDTO) {
    Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
    if (proveedorOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Proveedor proveedor = proveedorOpt.get();
    proveedor.setNombre(proveedorDTO.getNombre());
    proveedor.setCorreo(proveedorDTO.getCorreo());
    proveedor.setDireccion(proveedorDTO.getDireccion());
    proveedor.setTelefono(proveedorDTO.getTelefono());


    if (proveedorDTO.getProductoIds() != null) {
        List<Producto> productosExistentes = proveedor.getProductos() != null ? proveedor.getProductos() : new ArrayList<>();

        for (Integer productoId : proveedorDTO.getProductoIds()) {
            Optional<Producto> productoOpt = productoRepository.findById(productoId);
            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();
                producto.setProveedor(proveedor); 
                productosExistentes.add(producto); 
            }
        }

        
        proveedor.setProductos(productosExistentes);
    }

    
    Proveedor proveedorActualizado = proveedorRepository.save(proveedor);
    return ResponseEntity.ok(proveedorActualizado);
}
} 