package ecomarket.Ecomarket.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(toModel(savedCliente));
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
    public ResponseEntity<EntityModel<Cliente>> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ResponseEntity.ok(toModel(cliente));
    }

    // Obtener todos los clientes
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listarClientes() {
        List<EntityModel<Cliente>> clientes = clienteRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel()));
    }

    // Actualizar un cliente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteActualizado) {
        Cliente updatedCliente = clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setCorreo(clienteActualizado.getCorreo());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ResponseEntity.ok(toModel(updatedCliente));
    }

    // Eliminar un cliente
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtenerClientePorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).actualizarCliente(cliente.getId(), cliente)).withRel("actualizar"),
                linkTo(methodOn(ClienteController.class).eliminarCliente(cliente.getId())).withRel("eliminar"),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("clientes"));
    }
}
