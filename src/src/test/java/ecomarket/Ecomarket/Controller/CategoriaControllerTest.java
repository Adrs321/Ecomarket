package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.CategoriaDTO;
import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoriaControllerTest {

    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaRepository categoriaRepository;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNombre("Organico");

        categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNombre("TestC");
    }

    @Test
    public void testGuardar() {
        CategoriaDTO dto = new CategoriaDTO(null, "Organico");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria1);

        ResponseEntity<EntityModel<CategoriaDTO>> response = categoriaController.crearCategoria(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        CategoriaDTO result = response.getBody().getContent();
        assertEquals("Organico", result.getNombre());
        assertEquals(1L, result.getId());

        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testListar() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria1, categoria2));

        ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> response = categoriaController.listarCategorias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    public void testActualizar() {
        CategoriaDTO dto = new CategoriaDTO(null, "Organico");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria1);

        ResponseEntity<EntityModel<CategoriaDTO>> response = categoriaController.actualizarCategoria(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Organico", response.getBody().getContent().getNombre()); 

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testEliminar() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<?> response = categoriaController.eliminarCategoria(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoriaRepository, times(1)).existsById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testActualizarNoExistente() {
        CategoriaDTO dto = new CategoriaDTO(null, "Categoría no existe");
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<CategoriaDTO>> response = categoriaController.actualizarCategoria(99L, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoriaRepository, times(1)).findById(99L);
    }
}
