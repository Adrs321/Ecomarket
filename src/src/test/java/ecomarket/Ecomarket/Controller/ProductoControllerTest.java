package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    @InjectMocks
    private ProductoController productoController;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private Producto producto;
    private Proveedor proveedor;
    private Categoria categoria;
    private ProductoDTO productoDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setNombre("ProveedorTest");
        proveedor.setCorreo("proveedor@test.com");

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("CategoriaTest");

        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setDescripcion("Descripcion test");
        producto.setStock(10);
        producto.setProveedor(proveedor);
        producto.setCategoria(categoria);

        productoDTO = new ProductoDTO();
        productoDTO.setNombre("Producto Test");
        productoDTO.setPrecio(100.0);
        productoDTO.setDescripcion("Descripcion test");
        productoDTO.setStock(10);
        productoDTO.setProveedorId(1);
        productoDTO.setCategoriaId(1L);
    }

    @Test
    public void testAgregarProducto() {
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ResponseEntity<?> response = productoController.agregarProducto(productoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof EntityModel);
        
        EntityModel<Producto> responseBody = (EntityModel<Producto>) response.getBody();
        assertEquals("Producto Test", responseBody.getContent().getNombre());
    }



    @Test
    public void testListarProductos() {

        when(productoRepository.findAll()).thenReturn(List.of(producto));

 
        ResponseEntity<CollectionModel<EntityModel<Producto>>> response = 
            productoController.listarProductos();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void testBuscarPorNombre() {

        when(productoRepository.findByNombre("Producto Test")).thenReturn(Optional.of(producto));


        ResponseEntity<EntityModel<Producto>> response = 
            productoController.getByNombre("Producto Test");

  
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto Test", response.getBody().getContent().getNombre());
    }


    @Test
    public void testBuscarPorCategoria() {

        when(categoriaRepository.findByNombre("Organico")).thenReturn(Optional.of(categoria));
        when(productoRepository.findByCategoria(categoria)).thenReturn(List.of(producto));


        ResponseEntity<CollectionModel<EntityModel<Producto>>> response = 
            productoController.getByNombreCategoria("Organico");

    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

}
