package ecomarket.Ecomarket.DTO;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private Long id;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private ProductoDTO producto;
    private PedidoDTO pedido; // <--- AGREGA ESTE CAMPO

    public DetallePedidoDTO() {}

    public DetallePedidoDTO(Long id, Integer cantidad, Double precioUnitario, ProductoDTO producto, PedidoDTO pedido) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario != null ? BigDecimal.valueOf(precioUnitario) : null;
        this.producto = producto;
        this.pedido = pedido;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public ProductoDTO getProducto() { return producto; }
    public void setProducto(ProductoDTO producto) { this.producto = producto; }

    public PedidoDTO getPedido() { return pedido; }
    public void setPedido(PedidoDTO pedido) { this.pedido = pedido; }
}
