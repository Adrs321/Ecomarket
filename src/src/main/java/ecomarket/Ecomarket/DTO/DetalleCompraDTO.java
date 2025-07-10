package ecomarket.Ecomarket.DTO;

import java.time.LocalDate;

public class DetalleCompraDTO {
    private Integer id;
    private LocalDate fechaCompra;
    private Integer productoId;  // Cambiado a Long
    private Integer repartidorId;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Integer getProductoId() {  // Cambiado a Long
        return productoId;
    }

    public void setProductoId(Integer productoId) {  // Cambiado a Long
        this.productoId = productoId;
    }

    public Integer getRepartidorId() {
        return repartidorId;
    }

    public void setRepartidorId(Integer repartidorId) {
        this.repartidorId = repartidorId;
    }
}
