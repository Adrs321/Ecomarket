package ecomarket.Ecomarket.DTO;

public class PedidoDTO {
    private Long id;
    private String estado;
    private String direccionEntrega;
    private String metodoPago;
    private ClienteDTO cliente;
    private java.time.LocalDate fechaPedido;
    private java.time.LocalDate fechaEntrega;

    public PedidoDTO() {}

    public PedidoDTO(Long id, String estado, String direccionEntrega, String metodoPago, ClienteDTO cliente, java.time.LocalDate fechaPedido, java.time.LocalDate fechaEntrega) {
        this.id = id;
        this.estado = estado;
        this.direccionEntrega = direccionEntrega;
        this.metodoPago = metodoPago;
        this.cliente = cliente;
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

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
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
