package ecomarket.Ecomarket.DTO;

public class RepartidorDTO {
    private Integer id;
    private String nombre;
    private String telefono;
    private String patenteVehiculo;
    private String vehiculo;
    private String tipoVehiculo;
    private Integer idProveedor;
    
    
    
    public Integer getIdRepartidor() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getPatenteVehiculo() {
        return patenteVehiculo;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setIdRepartidor(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPatenteVehiculo(String patenteVehiculo) {
        this.patenteVehiculo = patenteVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }
        public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }
}


