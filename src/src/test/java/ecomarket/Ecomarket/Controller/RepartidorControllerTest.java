package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.RepartidorDTO;
import ecomarket.Ecomarket.Model.Proveedor;
import ecomarket.Ecomarket.Model.Repartidor;
import ecomarket.Ecomarket.Repositorio.ProveedorRepository;
import ecomarket.Ecomarket.Repositorio.RepartidorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepartidorControllerTest {

    @InjectMocks
    private RepartidorController repartidorController;

    @Mock
    private RepartidorRepository repartidorRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAgregarRepartidor() {
        
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Juan Perez");
        dto.setTelefono("987654321");
        dto.setTipoVehiculo("Moto");
        dto.setPatenteVehiculo("ABC123");
        dto.setIdProveedor(1);

        Proveedor proveedor = new Proveedor();
        proveedor.setId(1);

        Repartidor repartidorGuardado = new Repartidor();
        repartidorGuardado.setId(1);
        repartidorGuardado.setNombre(dto.getNombre());
        repartidorGuardado.setTelefono(dto.getTelefono());
        repartidorGuardado.setTipoVehiculo(dto.getTipoVehiculo());
        repartidorGuardado.setPatente(dto.getPatenteVehiculo());
        repartidorGuardado.setProveedor(proveedor);

       
        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidorGuardado);

        
        ResponseEntity<EntityModel<Repartidor>> response = repartidorController.agregar(dto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Repartidor result = response.getBody().getContent();
        assertEquals("Juan Perez", result.getNombre());
        assertEquals("Moto", result.getTipoVehiculo());
        assertEquals(1, result.getProveedor().getId());
    }

    @Test
    public void testAgregarRepartidorConProveedor() {
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Juan Perez");
        dto.setTelefono("987654321");
        dto.setTipoVehiculo("Moto");
        dto.setPatenteVehiculo("ABC123");
       

        ResponseEntity<EntityModel<Repartidor>> response = repartidorController.agregar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testEliminarRepartidor() {
        
        ResponseEntity<?> response = repartidorController.eliminarRepartidor(1);
        
        verify(repartidorRepository).deleteById(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testActualizarRepartidor() {
       
        Integer id = 1;
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Juan Perez Actualizado");
        dto.setTelefono("987654321");
        dto.setTipoVehiculo("Moto");
        dto.setPatenteVehiculo("ABC123");
        dto.setIdProveedor(2);

        Proveedor nuevoProveedor = new Proveedor();
        nuevoProveedor.setId(2);

        Repartidor repartidorExistente = new Repartidor();
        repartidorExistente.setId(id);
        repartidorExistente.setNombre("Juan Perez");
        repartidorExistente.setTelefono("123456789");
        repartidorExistente.setTipoVehiculo("Auto");
        repartidorExistente.setPatente("XYZ789");

        Repartidor repartidorActualizado = new Repartidor();
        repartidorActualizado.setId(id);
        repartidorActualizado.setNombre(dto.getNombre());
        repartidorActualizado.setTelefono(dto.getTelefono());
        repartidorActualizado.setTipoVehiculo(dto.getTipoVehiculo());
        repartidorActualizado.setPatente(dto.getPatenteVehiculo());
        repartidorActualizado.setProveedor(nuevoProveedor);

        
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidorExistente));
        when(proveedorRepository.findById(2)).thenReturn(Optional.of(nuevoProveedor));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidorActualizado);

       
        ResponseEntity<EntityModel<Repartidor>> response = repartidorController.actualizarRepartidor(id, dto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Repartidor result = response.getBody().getContent();
        assertEquals("Juan Perez Actualizado", result.getNombre());
        assertEquals("Moto", result.getTipoVehiculo());
        assertEquals(2, result.getProveedor().getId());
    }

    @Test
    public void testActualizarRepartidorProveedorCambio() {
        
        Integer id = 1;
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Juan Perez Actualizado");
        dto.setTelefono("987654321");
        dto.setTipoVehiculo("Moto");
        dto.setPatenteVehiculo("ABC123");
        
        Repartidor repartidorExistente = new Repartidor();
        repartidorExistente.setId(id);
        repartidorExistente.setNombre("Juan Perez");
        repartidorExistente.setTelefono("123456789");
        repartidorExistente.setTipoVehiculo("Auto");
        repartidorExistente.setPatente("XYZ789");
        Proveedor proveedorOriginal = new Proveedor();
        proveedorOriginal.setId(1);
        repartidorExistente.setProveedor(proveedorOriginal);

        Repartidor repartidorActualizado = new Repartidor();
        repartidorActualizado.setId(id);
        repartidorActualizado.setNombre(dto.getNombre());
        repartidorActualizado.setTelefono(dto.getTelefono());
        repartidorActualizado.setTipoVehiculo(dto.getTipoVehiculo());
        repartidorActualizado.setPatente(dto.getPatenteVehiculo());
        repartidorActualizado.setProveedor(proveedorOriginal);

        
        when(repartidorRepository.findById(id)).thenReturn(Optional.of(repartidorExistente));
        when(repartidorRepository.save(any(Repartidor.class))).thenReturn(repartidorActualizado);

       
        ResponseEntity<EntityModel<Repartidor>> response = repartidorController.actualizarRepartidor(id, dto);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Repartidor result = response.getBody().getContent();
        assertEquals(1, result.getProveedor().getId()); 
    }
}