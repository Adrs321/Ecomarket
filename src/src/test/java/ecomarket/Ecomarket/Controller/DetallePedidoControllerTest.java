package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.DetallePedidoDTO;
import ecomarket.Ecomarket.DTO.ProductoDTO;
import ecomarket.Ecomarket.Model.DetallePedido;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Repositorio.DetallePedidoRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DetallePedidoControllerTest {

    @InjectMocks
    private DetallePedidoController detallePedidoController;

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    private DetallePedido detallePedido;
    private DetallePedidoDTO detallePedidoDTO;
    private Producto producto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

 
        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setDescripcion("Descripción test");
        producto.setStock(10);

        detallePedido = new DetallePedido();
        detallePedido.setId(1L);
        detallePedido.setCantidad(2);
        detallePedido.setPrecioUnitario(100.0);
        detallePedido.setProducto(producto);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1);
        productoDTO.setNombre("Producto Test");
        productoDTO.setPrecio(100.0);
        productoDTO.setDescripcion("Descripción test");
        productoDTO.setStock(10);

        detallePedidoDTO = new DetallePedidoDTO();
        detallePedidoDTO.setId(1L);
        detallePedidoDTO.setCantidad(2);
        detallePedidoDTO.setPrecioUnitario(BigDecimal.valueOf(100.0));
        detallePedidoDTO.setProducto(productoDTO);
    }

    @Test
    public void testCrearDetallePedido() {

        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(detallePedido);

        // Ejecutar
        ResponseEntity<EntityModel<DetallePedidoDTO>> response = 
            detallePedidoController.crearDetallePedido(detallePedidoDTO);

        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testCrearDetallePedidoWithoutProducto() {
        detallePedidoDTO.setProducto(null);
        
        assertThrows(RuntimeException.class, () -> {
            detallePedidoController.crearDetallePedido(detallePedidoDTO);
        });
    }


    @Test
    public void testListarDetallesPedidos() {
        // Configurar mock
        when(detallePedidoRepository.findAll()).thenReturn(List.of(detallePedido));

        // Ejecutar
        ResponseEntity<CollectionModel<EntityModel<DetallePedidoDTO>>> response = 
            detallePedidoController.listarDetallesPedidos();

        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }


    @Test
    public void testActualizarDetallePedido() {
        // Configurar mocks
        when(detallePedidoRepository.findById(1L)).thenReturn(Optional.of(detallePedido));
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(detallePedido);

        // Ejecutar
        ResponseEntity<EntityModel<DetallePedidoDTO>> response = 
            detallePedidoController.actualizarDetallePedido(1L, detallePedido);

        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
    }


}