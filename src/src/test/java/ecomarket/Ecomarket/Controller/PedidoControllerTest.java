package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.ClienteDTO;
import ecomarket.Ecomarket.DTO.PedidoCreateDTO;
import ecomarket.Ecomarket.DTO.PedidoDTO;
import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private Pedido pedido;
    private PedidoCreateDTO pedidoCreateDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setCorreo("juan@example.com");

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setEstado("PENDIENTE");
        pedido.setDireccionEntrega("Calle Falsa 123");
        pedido.setMetodoPago("TARJETA");
        pedido.setFechaPedido(LocalDate.now());

        pedidoCreateDTO = new PedidoCreateDTO();
        pedidoCreateDTO.setClienteId(1L);
        pedidoCreateDTO.setEstado("PENDIENTE");
        pedidoCreateDTO.setDireccionEntrega("Calle Falsa 123");
        pedidoCreateDTO.setMetodoPago("TARJETA");
    }

    @Test
    public void testCrearPedido() {
        
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

       
        ResponseEntity<EntityModel<PedidoDTO>> response = pedidoController.crearPedido(pedidoCreateDTO);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        PedidoDTO responseDTO = response.getBody().getContent();
        assertEquals(1L, responseDTO.getId());
        assertEquals("PENDIENTE", responseDTO.getEstado());
        assertNotNull(responseDTO.getCliente());
        assertEquals(1L, responseDTO.getCliente().getId());
    }

    @Test
    public void testCrearPedidoClienteId() {
        pedidoCreateDTO.setClienteId(null);
        
        assertThrows(RuntimeException.class, () -> {
            pedidoController.crearPedido(pedidoCreateDTO);
        });
    }

    @Test
    public void testListarTodosLosPedidos() {
        
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        
        ResponseEntity<CollectionModel<EntityModel<PedidoDTO>>> response = 
            pedidoController.listarTodosLosPedidos();

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void testObtenerPedidoPorId() {
        
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        
        ResponseEntity<EntityModel<PedidoDTO>> response = 
            pedidoController.obtenerPedidoPorId(1L);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testActualizarPedido() {
        
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        
        ResponseEntity<EntityModel<PedidoDTO>> response = 
            pedidoController.actualizarPedido(1L, pedidoCreateDTO);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testActualizarPedidoClienteId() {
        pedidoCreateDTO.setClienteId(null);
        
        assertThrows(RuntimeException.class, () -> {
            pedidoController.actualizarPedido(1L, pedidoCreateDTO);
        });
    }
}
