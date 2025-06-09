package ecomarket.Ecomarket.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "proveedor_id") 
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    @JsonBackReference
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private Categoria categoria;

}

