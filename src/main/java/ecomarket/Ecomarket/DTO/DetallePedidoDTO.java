package ecomarket.Ecomarket.DTO;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private Long id;
    private Integer cantidad;
    private java.math.BigDecimal precioUnitario;
    private ProductoDTO producto;

    public DetallePedidoDTO() {}

 public DetallePedidoDTO(Long id, Integer cantidad, Double precioUnitario, ProductoDTO producto) {
    this.id = id;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario != null ? BigDecimal.valueOf(precioUnitario) : null;
    this.producto = producto;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }
}
