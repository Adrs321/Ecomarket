package ecomarket.Ecomarket.DTO;

import java.util.List;

public class ClienteDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
    private String contrasena;
    private List<String> pedidos;

    public ClienteDTO() {}

    public ClienteDTO(Long id, String nombre, String correo, String direccion, String telefono, String contrasena, List<String> pedidos) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.pedidos = pedidos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<String> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<String> pedidos) {
        this.pedidos = pedidos;
    }
}
