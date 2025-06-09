package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        if (producto.getProveedor() == null || producto.getProveedor().getId() == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar el ID del proveedor");
        }

        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(producto.getProveedor().getId());
        if (proveedorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Proveedor no encontrado");
        }

        producto.setProveedor(proveedorOpt.get()); 

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    @GetMapping("/listar")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }
    

    @DeleteMapping("/productos/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {   
        Optional<Producto> productoOpt = productoRepository.findById(id);
    
    if (productoOpt.isEmpty()) {
        return ResponseEntity.notFound().build(); 
    }

    Producto producto = productoOpt.get();

    
    if (producto.getProveedor() != null) {
        producto.setProveedor(null); 
        productoRepository.save(producto); 
    }

    
    productoRepository.deleteById(id);

    return ResponseEntity.noContent().build(); 
}
    @GetMapping("/buscarPorNombre/{nombre}")
    public Producto getByNombre(@PathVariable String nombre) {
        return productoRepository.findByNombre(nombre).orElse(null);
    }
    @Autowired
    private CategoriaRepository categoriaRepository;

    //Buscar producto por Categoria
    @GetMapping("/buscarPorCategoria/{nombre}")
    public Iterable<Producto> getByNombreCategoria(@PathVariable String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return productoRepository.findByCategoria(categoria);
    }
}


