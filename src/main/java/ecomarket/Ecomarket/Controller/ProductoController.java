package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProducto(@RequestBody ProductoDTO productoDTO) {
        if (productoDTO.getNombre() == null || productoDTO.getPrecio() == null || 
            productoDTO.getProveedorId() == null) {
            return ResponseEntity.badRequest().body("Nombre, precio y proveedorId son obligatorios");
        }

        Proveedor proveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock() != null ? productoDTO.getStock() : 0);
        producto.setProveedor(proveedor);

        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto savedProducto = productoRepository.save(producto);
        return ResponseEntity.ok(toModel(savedProducto));
    }

    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos() {
        List<EntityModel<Producto>> productos = productoRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel()));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        return productoRepository.findById(id)
            .map(producto -> {
                if (producto.getProveedor() != null) {
                    producto.getProveedor().getProductos().remove(producto);
                }
                if (producto.getCategoria() != null) {
                    producto.getCategoria().getProductos().remove(producto);
                }
                
                productoRepository.delete(producto);
                return ResponseEntity.noContent().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarPorNombre/{nombre}")
    public ResponseEntity<EntityModel<Producto>> getByNombre(@PathVariable String nombre) {
        return productoRepository.findByNombre(nombre)
            .map(this::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarPorCategoria/{nombre}")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getByNombreCategoria(@PathVariable String nombre) {
        Categoria categoria = categoriaRepository.findByNombre(nombre)
            .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        
        List<EntityModel<Producto>> productos = StreamSupport.stream(
                productoRepository.findByCategoria(categoria).spliterator(), false)
            .map(this::toModel)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(CollectionModel.of(productos,
            linkTo(methodOn(ProductoController.class).getByNombreCategoria(nombre)).withSelfRel()));
    }

    private EntityModel<Producto> toModel(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        EntityModel<Producto> model = EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).getByNombre(producto.getNombre())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).agregarProducto(null)).withRel("agregar"),
            linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("eliminar"),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));
        
        return model;
    }
}
