package ecomarket.Ecomarket.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTOrequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Integer proveedorId; 
    private Long categoriaId; 
}