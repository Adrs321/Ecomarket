package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.CalificacionDTO;
import ecomarket.Ecomarket.Model.Calificacion;
import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Repositorio.CalificacionRepository;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CalificacionControllerTest {

    @InjectMocks
    private CalificacionController calificacionController;

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProductoRepository productoRepository;

    private Calificacion calificacion;
    private CalificacionDTO calificacionDTO;
    private Cliente cliente;
    private Producto producto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");

        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto 1");

        calificacion = new Calificacion();
        calificacion.setId(1);
        calificacion.setPuntaje(5);
        calificacion.setComentario("Excelente producto");
        calificacion.setFecha(LocalDateTime.now());
        calificacion.setCliente(cliente);
        calificacion.setProducto(producto);

        calificacionDTO = new CalificacionDTO();
        calificacionDTO.setId(1);
        calificacionDTO.setPuntaje(5);
        calificacionDTO.setComentario("Excelente producto");
        calificacionDTO.setClienteId(1L);
        calificacionDTO.setProductoId(1);
    }

    @Test
    public void testCrearCalificacion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);

        ResponseEntity<EntityModel<CalificacionDTO>> response = 
            calificacionController.crearCalificacion(calificacionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getContent().getPuntaje());
        assertEquals("Excelente producto", response.getBody().getContent().getComentario());
    }



    @Test
    public void testListarCalificaciones() {

        when(calificacionRepository.findAll()).thenReturn(List.of(calificacion));


        ResponseEntity<List<Calificacion>> response = 
            calificacionController.listarCalificaciones();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(5, response.getBody().get(0).getPuntaje());
    }

    @Test
    public void testObtenerPorProducto() {

        when(calificacionRepository.findByProductoId(1)).thenReturn(List.of(calificacion));


        ResponseEntity<CollectionModel<EntityModel<CalificacionDTO>>> response = 
            calificacionController.obtenerPorProducto(1);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void testObtenerPromedio() {

        when(calificacionRepository.findByProductoId(1)).thenReturn(List.of(calificacion));


        ResponseEntity<Double> response = 
            calificacionController.obtenerPromedio(1);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5.0, response.getBody());
    }

    @Test
    public void testEliminarCalificacion() {

        when(calificacionRepository.existsById(1)).thenReturn(true);

 
        ResponseEntity<?> response = 
            calificacionController.eliminar(1);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(calificacionRepository, times(1)).deleteById(1);
    }


    @Test
    public void testActualizarCalificacion() {

        when(calificacionRepository.findById(1)).thenReturn(Optional.of(calificacion));
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);


        calificacionDTO.setComentario("Muy bueno");
        calificacionDTO.setPuntaje(4);


        ResponseEntity<EntityModel<CalificacionDTO>> response = 
            calificacionController.actualizar(1, calificacionDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getContent().getPuntaje());
        assertEquals("Muy bueno", response.getBody().getContent().getComentario());
    }

}
