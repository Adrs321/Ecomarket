package ecomarket.Ecomarket.DTO;

public class PedidoCreateDTO {
    private Long id;
    private String estado;
    private String direccionEntrega;
    private String metodoPago;
    private Long clienteId;
    private java.time.LocalDate fechaPedido;
    private java.time.LocalDate fechaEntrega;

    public PedidoCreateDTO() {}

    public PedidoCreateDTO(Long id, String estado, String direccionEntrega, String metodoPago, Long clienteId, java.time.LocalDate fechaPedido, java.time.LocalDate fechaEntrega) {
        this.id = id;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.metodoPago = metodoPago;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public java.time.LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(java.time.LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public java.time.LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(java.time.LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
