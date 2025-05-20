package ecomarket.Ecomarket.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;
    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private TipoCuenta tipoCuenta;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false, length = 10)
    private EstadoCuenta estadoCuenta = EstadoCuenta.Activo;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    public enum TipoCuenta {
        Cliente,
        Gerente,
        Administrador,
        EmpleadoVentas,
        Logistica
    }
    public enum EstadoCuenta {
        Activo,
        Inactivo
    }
}