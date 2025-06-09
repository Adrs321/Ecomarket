package ecomarket.Ecomarket.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.Model.Cliente;
import ecomarket.Ecomarket.Repositorio.ClienteRepository;
import ecomarket.Ecomarket.DTO.LoginRequest;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear un nuevo cliente
    @PostMapping("/crear")
    public Cliente crearCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Login method corrected with proper variables
    @PostMapping("/login")
    public ResponseEntity<String> loginCliente(@RequestBody LoginRequest loginRequest) {
        Cliente cliente = clienteRepository.findByCorreo(loginRequest.getCorreo());
        if (cliente == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas: cliente no encontrado");
        }
        if (cliente.getContrasena() != null && cliente.getContrasena().equals(loginRequest.getContrasena())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas: contraseña incorrecta");
        }
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    // Obtener todos los clientes
    @GetMapping("/listar")
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Actualizar un cliente
    @PutMapping("/actualizar/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setCorreo(clienteActualizado.getCorreo());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    // Eliminar un cliente
    @DeleteMapping("/eliminar/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
    }
}
