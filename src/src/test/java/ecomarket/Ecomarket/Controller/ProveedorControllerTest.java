package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.ProveedorDTO;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Proveedor;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ProveedorControllerTest {

    @InjectMocks
    private ProveedorController proveedorController;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private ProductoRepository productoRepository;

    private Proveedor proveedor;
    private ProveedorDTO proveedorDTO;
    private Producto producto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

  
        proveedor = new Proveedor();
        proveedor.setId(1);
        proveedor.setNombre("Proveedor Test");
        proveedor.setCorreo("proveedor@test.com");
        proveedor.setDireccion("Calle 123");
        proveedor.setTelefono("123456789");

        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto Test");
        producto.setProveedor(proveedor);

        proveedor.setProductos(new ArrayList<>(Arrays.asList(producto)));

        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setIdProveedor(1);
        proveedorDTO.setNombre("Proveedor Test");
        proveedorDTO.setCorreo("proveedor@test.com");
        proveedorDTO.setDireccion("Calle 123");
        proveedorDTO.setTelefono("123456789");
        proveedorDTO.setProductoIds(Arrays.asList(1));
    }

    @Test
    public void testCrearProveedor() {
        when(productoRepository.findAllById(anyList())).thenReturn(Arrays.asList(producto));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);


        ResponseEntity<EntityModel<ProveedorDTO>> response = proveedorController.crearProveedor(proveedorDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        ProveedorDTO responseDTO = response.getBody().getContent();
        assertEquals(1, responseDTO.getIdProveedor());
        assertEquals("Proveedor Test", responseDTO.getNombre());
        assertEquals(1, responseDTO.getProductoIds().size());
    }

    @Test
    public void testActualizarProveedor() {

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(productoRepository.findAllById(anyList())).thenReturn(Arrays.asList(producto));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);


        ResponseEntity<EntityModel<ProveedorDTO>> response = 
            proveedorController.actualizarProveedor(1, proveedorDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().getIdProveedor());
    }


    @Test
    public void testEliminarProveedor() {

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        doNothing().when(proveedorRepository).deleteById(1);


        ResponseEntity<?> response = proveedorController.eliminarProveedor(1);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(proveedorRepository, times(1)).deleteById(1);
    }


    @Test
    public void testListarProveedores() {

        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(proveedor));


        ResponseEntity<CollectionModel<EntityModel<ProveedorDTO>>> response = 
            proveedorController.listarProveedores();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }


}
