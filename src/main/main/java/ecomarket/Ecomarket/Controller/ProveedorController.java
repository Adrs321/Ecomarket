package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.DTO.ProveedorDTO;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/agregar")
    public ResponseEntity<EntityModel<ProveedorDTO>> crearProveedor(@RequestBody ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());

        if (dto.getProductoIds() != null) {
            List<Producto> productos = productoRepository.findAllById(dto.getProductoIds());
            productos.forEach(p -> p.setProveedor(proveedor));
            proveedor.setProductos(productos);
        }

        Proveedor guardado = proveedorRepository.save(proveedor);

        return ResponseEntity.ok(toModel(convertToDTO(guardado)));
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<ProveedorDTO>> actualizarProveedor(@PathVariable Integer id, @RequestBody ProveedorDTO dto) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proveedor proveedor = proveedorOpt.get();
        proveedor.setNombre(dto.getNombre());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());

       
        List<Producto> productosActuales = proveedor.getProductos() != null ? proveedor.getProductos() : new ArrayList<>();
        productosActuales.clear(); 

        if (dto.getProductoIds() != null) {
            List<Producto> nuevosProductos = productoRepository.findAllById(dto.getProductoIds());
            nuevosProductos.forEach(p -> p.setProveedor(proveedor));
            productosActuales.addAll(nuevosProductos);
        }

        proveedor.setProductos(productosActuales);
        Proveedor actualizado = proveedorRepository.save(proveedor);

        return ResponseEntity.ok(toModel(convertToDTO(actualizado)));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Integer id) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(id);
        if (proveedorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proveedor proveedor = proveedorOpt.get();
        if (proveedor.getProductos() != null) {
            proveedor.getProductos().forEach(p -> p.setProveedor(null));
        }

        proveedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<ProveedorDTO>>> listarProveedores() {
        List<EntityModel<ProveedorDTO>> lista = proveedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(ProveedorController.class).listarProveedores()).withSelfRel()));
    }

private ProveedorDTO convertToDTO(Proveedor proveedor) {
    ProveedorDTO dto = new ProveedorDTO();
    dto.setIdProveedor(proveedor.getId());  
    dto.setNombre(proveedor.getNombre());
    dto.setCorreo(proveedor.getCorreo());
    dto.setDireccion(proveedor.getDireccion());
    dto.setTelefono(proveedor.getTelefono());
    
    if(proveedor.getProductos() != null) {
        dto.setProductoIds(proveedor.getProductos().stream()
            .map(Producto::getId)
            .collect(Collectors.toList()));
    } else {
        dto.setProductoIds(new ArrayList<>());
    }
    
    
    return dto;
}


    private EntityModel<ProveedorDTO> toModel(ProveedorDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ProveedorController.class).listarProveedores()).withRel("lista"),
                linkTo(methodOn(ProveedorController.class).crearProveedor(new ProveedorDTO())).withRel("crear"),
                linkTo(methodOn(ProveedorController.class).actualizarProveedor(dto.getIdProveedor(), new ProveedorDTO())).withRel("actualizar"),
                linkTo(methodOn(ProveedorController.class).eliminarProveedor(dto.getIdProveedor())).withRel("eliminar"));
    }
}
