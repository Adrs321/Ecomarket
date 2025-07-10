package ecomarket.Ecomarket.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaPedido;
    private String estado;
    private LocalDate fechaEntrega;
    private String direccionEntrega;
    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "cupon_id")
    private Cupon cupon;

    // @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    // @JsonManagedReference
    // private List<DetallePedido> detalles;
}

