package ecomarket.Ecomarket.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecomarket.Ecomarket.DTO.FacturaDTO;
import ecomarket.Ecomarket.Model.Factura;
import ecomarket.Ecomarket.Repositorio.FacturaRepository;

public class FacturaControllerUnitTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaController facturaController;

    private Factura factura1;
    private Factura factura2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        factura1 = new Factura();
        factura1.setId(1L);
        factura1.setFechaEmision(LocalDate.of(2023, 1, 1));
        factura1.setMontoTotal(100.0);

        factura2 = new Factura();
        factura2.setId(2L);
        factura2.setFechaEmision(LocalDate.of(2023, 2, 1));
        factura2.setMontoTotal(200.0);
    }

    @Test
    public void testListarFacturas() {
        when(facturaRepository.findAll()).thenReturn(Arrays.asList(factura1, factura2));

        var response = facturaController.listarFacturas();
        assertNotNull(response);
        assertTrue(response.getBody().getContent().size() == 2);
    }

    @Test
    public void testObtenerFacturaPorId_Found() {
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura1));

        var response = facturaController.obtenerFacturaPorId(1L);
        assertNotNull(response);
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testObtenerFacturaPorId_NotFound() {
        when(facturaRepository.findById(3L)).thenThrow(new RuntimeException("Factura no encontrada"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            facturaController.obtenerFacturaPorId(3L);
        });

        assertEquals("Factura no encontrada", exception.getMessage());
    }

    @Test
    public void testCrearFactura() {
        FacturaDTO dto = new FacturaDTO();
        dto.setFechaEmision(LocalDate.of(2023, 3, 1));
        dto.setMontoTotal(300.0);

        Factura facturaToSave = new Factura();
        facturaToSave.setFechaEmision(dto.getFechaEmision());
        facturaToSave.setMontoTotal(dto.getMontoTotal());

        Factura savedFactura = new Factura();
        savedFactura.setId(3L);
        savedFactura.setFechaEmision(dto.getFechaEmision());
        savedFactura.setMontoTotal(dto.getMontoTotal());

        when(facturaRepository.save(any(Factura.class))).thenReturn(savedFactura);

        var response = facturaController.crearFactura(dto);
        assertNotNull(response);
        assertEquals(3L, response.getBody().getContent().getId());
    }

    @Test
    public void testActualizarFactura_Found() {
        FacturaDTO dto = new FacturaDTO();
        dto.setFechaEmision(LocalDate.of(2023, 4, 1));
        dto.setMontoTotal(400.0);

        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura1));
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura1);

        var response = facturaController.actualizarFactura(1L, dto);
        assertNotNull(response);
        assertEquals(dto.getFechaEmision(), response.getBody().getContent().getFechaEmision());
        assertEquals(dto.getMontoTotal(), response.getBody().getContent().getMontoTotal());
    }

    @Test
    public void testActualizarFactura_NotFound() {
        FacturaDTO dto = new FacturaDTO();
        dto.setFechaEmision(LocalDate.of(2023, 5, 1));
        dto.setMontoTotal(500.0);

        when(facturaRepository.findById(5L)).thenThrow(new RuntimeException("Factura no encontrada"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            facturaController.actualizarFactura(5L, dto);
        });

        assertEquals("Factura no encontrada", exception.getMessage());
    }
}
