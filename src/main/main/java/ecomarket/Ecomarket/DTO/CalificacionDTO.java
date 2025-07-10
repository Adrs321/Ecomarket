package ecomarket.Ecomarket.DTO;

import java.time.LocalDateTime;


public class CalificacionDTO {
    private Integer id;
    private Integer puntaje;
    private String comentario;
    private LocalDateTime fecha;
    private Long clienteId;
    private Integer productoId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
}

