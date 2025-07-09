package ecomarket.Ecomarket.Controller;

import ecomarket.Ecomarket.DTO.CategoriaDTO;
import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Crear categoría
    @PostMapping("/grabar")
    public ResponseEntity<EntityModel<CategoriaDTO>> crearCategoria(@RequestBody CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());

        Categoria saved = categoriaRepository.save(categoria);
        CategoriaDTO responseDto = new CategoriaDTO(saved.getId(), saved.getNombre());

        return ResponseEntity.ok(toModel(responseDto));
    }

    // Listar categorías
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> listarCategorias() {
        List<EntityModel<CategoriaDTO>> categorias = categoriaRepository.findAll().stream()
                .map(c -> toModel(new CategoriaDTO(c.getId(), c.getNombre())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withSelfRel()));
    }

    // Actualizar categoría
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizarCategoria(@PathVariable Long id,
                                                                          @RequestBody CategoriaDTO dto) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
        if (optionalCategoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria = optionalCategoria.get();
        categoria.setNombre(dto.getNombre());
        Categoria updated = categoriaRepository.save(categoria);

        CategoriaDTO updatedDto = new CategoriaDTO(updated.getId(), updated.getNombre());
        return ResponseEntity.ok(toModel(updatedDto));
    }

    // Eliminar categoría
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Convertir DTO a EntityModel con links HATEOAS
    private EntityModel<CategoriaDTO> toModel(CategoriaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(CategoriaController.class).listarCategorias()).withRel("listar"),
                linkTo(methodOn(CategoriaController.class).crearCategoria(dto)).withRel("crear"),
                linkTo(methodOn(CategoriaController.class).actualizarCategoria(dto.getId(), dto)).withRel("actualizar"),
                linkTo(methodOn(CategoriaController.class).eliminarCategoria(dto.getId())).withRel("eliminar"));
    }
}
