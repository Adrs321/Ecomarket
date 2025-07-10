package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.LoginRequest;
import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteRepository clienteRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
       
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Juan Pérez");
        cliente1.setCorreo("juan@example.com");
        cliente1.setTelefono("123456789");
        cliente1.setContrasena("password123");

        cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombre("María García");
        cliente2.setCorreo("maria@example.com");
        cliente2.setTelefono("987654321");
        cliente2.setContrasena("securepass");

        loginRequest = new LoginRequest();
        loginRequest.setCorreo("juan@example.com");
        loginRequest.setContrasena("password123");
    }

    @Test
    public void testCrearCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1);
        
        ResponseEntity<EntityModel<Cliente>> response = clienteController.crearCliente(cliente1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
        assertEquals("Juan Pérez", response.getBody().getContent().getNombre());
        
        verify(clienteRepository, times(1)).save(cliente1);
    }

    @Test
    public void testLoginCliente() {
        when(clienteRepository.findByCorreo("juan@example.com")).thenReturn(cliente1);
        
        ResponseEntity<String> response = clienteController.loginCliente(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inicio de sesión exitoso", response.getBody());
    }


    @Test
    public void testObtenerClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        
        ResponseEntity<EntityModel<Cliente>> response = clienteController.obtenerClientePorId(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testObtenerClientePorIdNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            clienteController.obtenerClientePorId(99L);
        });
    }

    @Test
    public void testListarClientes() {
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);
        
        ResponseEntity<CollectionModel<EntityModel<Cliente>>> response = clienteController.listarClientes();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testActualizarCliente() {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Juan Pérez Actualizado");
        clienteActualizado.setCorreo("juan.nuevo@example.com");
        clienteActualizado.setTelefono("111111111");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);
        
        ResponseEntity<EntityModel<Cliente>> response = clienteController.actualizarCliente(1L, clienteActualizado);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Pérez Actualizado", response.getBody().getContent().getNombre());
    }


    @Test
    public void testEliminarCliente() {
        
        ResponseEntity<?> response = clienteController.eliminarCliente(1L);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}
