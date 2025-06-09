package ecomarket.Ecomarket.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.Model.Categoria;
import ecomarket.Ecomarket.Repositorio.CategoriaRepository;





@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Crear categoria
    @PostMapping("/grabar")
    public Categoria postSave(@RequestBody Categoria categoria) {
        System.out.println("Categoria: " + categoria);
        return categoriaRepository.save(categoria);        
        
    }
    // Listar categorias
    @GetMapping("/listar")
    public Iterable<Categoria> getAll() {
        return categoriaRepository.findAll();
    }
    // Buscar categoria
    @PutMapping("/actualizar/{id}")
    public Categoria putUpdate(@RequestBody Categoria categoria, @PathVariable Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("La categoría con ID " + id + " no existe.");
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }
    // Eliminar categoria
    @DeleteMapping("/eliminar/{id}")
    public void delete(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("La categoría con ID " + id + " no existe.");
        }
    }
}
