package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.DetalleCompraDTO;
import ecomarket.Ecomarket.Model.DetalleCompra;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Model.Repartidor;
import ecomarket.Ecomarket.Repositorio.DetalleCompraRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;
import ecomarket.Ecomarket.Repositorio.RepartidorRepository;
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
import static org.mockito.Mockito.*;

public class DetalleCompraControllerTest {

    @InjectMocks
    private DetalleCompraController detalleCompraController;

    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private RepartidorRepository repartidorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAgregarDetalleCompra() {
        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setProductoId(1);
        dto.setRepartidorId(1);

        Producto producto = new Producto();
        producto.setId(1);

        Repartidor repartidor = new Repartidor();
        repartidor.setId(1);

        DetalleCompra detalleGuardado = new DetalleCompra();
        detalleGuardado.setId(1);
        detalleGuardado.setProducto(producto);
        detalleGuardado.setRepartidor(repartidor);


        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(repartidorRepository.findById(1)).thenReturn(Optional.of(repartidor));
        when(detalleCompraRepository.save(any(DetalleCompra.class))).thenReturn(detalleGuardado);


        ResponseEntity<EntityModel<DetalleCompra>> response = detalleCompraController.agregarDetalle(dto);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        DetalleCompra result = response.getBody().getContent();
        assertEquals(1, result.getProducto().getId());
        assertEquals(1, result.getRepartidor().getId());
    }



    @Test
    public void testListarDetallesCompra() {

        when(detalleCompraRepository.findAll()).thenReturn(List.of(new DetalleCompra(), new DetalleCompra()));


        ResponseEntity<CollectionModel<EntityModel<DetalleCompra>>> response = detalleCompraController.listarDetalles();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testActualizarDetalleCompra() {

        Integer id = 1;
        DetalleCompraDTO dto = new DetalleCompraDTO();
        dto.setProductoId(2);
        dto.setRepartidorId(2);

        DetalleCompra detalleExistente = new DetalleCompra();
        detalleExistente.setId(id);
        detalleExistente.setProducto(new Producto());
        detalleExistente.setRepartidor(new Repartidor());

        Producto nuevoProducto = new Producto();
        nuevoProducto.setId(2);

        Repartidor nuevoRepartidor = new Repartidor();
        nuevoRepartidor.setId(2);

        DetalleCompra detalleActualizado = new DetalleCompra();
        detalleActualizado.setId(id);
        detalleActualizado.setProducto(nuevoProducto);
        detalleActualizado.setRepartidor(nuevoRepartidor);


        when(detalleCompraRepository.findById(id)).thenReturn(Optional.of(detalleExistente));
        when(productoRepository.findById(2)).thenReturn(Optional.of(nuevoProducto));
        when(repartidorRepository.findById(2)).thenReturn(Optional.of(nuevoRepartidor));
        when(detalleCompraRepository.save(any(DetalleCompra.class))).thenReturn(detalleActualizado);


        ResponseEntity<?> response = detalleCompraController.actualizarDetalle(id, dto);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof EntityModel);
    }

    @Test
    public void testEliminarDetalleCompra() {
        Integer id = 1;
        
        when(detalleCompraRepository.existsById(id)).thenReturn(true);

        ResponseEntity<?> response = detalleCompraController.eliminarDetalle(id);

        verify(detalleCompraRepository).deleteById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Detalle eliminado", response.getBody());
    }
}