package ecomarket.Ecomarket;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ecomarket.Ecomarket.Model.Usuario;
import ecomarket.Ecomarket.Repositorio.UsuarioRepository;

@SpringBootTest
class EcomarketApplicationTests {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Test
	void grabar() {
       Usuario usuario = new Usuario();
	   usuario.setNombre("Juan Perez");
	   usuario.setCorreoElectronico("Juan@gmail.com");
	   usuario.setContraseña("123456");
	   usuario.setTipoCuenta(Usuario.TipoCuenta.Cliente);
	   usuario.setEstadoCuenta(Usuario.EstadoCuenta.Activo);
	   usuario.setFechaCreacion(LocalDateTime.now());
	   usuarioRepository.save(usuario);

	}

	@Test
	void testMain() {
		
	}
	@Test
	void Modificar() {
		Usuario usuario = usuarioRepository.findById( 14).orElse(null);
		Assertions.assertThat(usuario).isNotNull();
		if (usuario != null) {
			usuario.setNombre("Juanito Perez");
			usuario.setCorreoElectronico("juanito@gmail.com");
			usuario.setContraseña("1234567");
			usuario.setTipoCuenta(Usuario.TipoCuenta.Administrador);
			usuario.setEstadoCuenta(Usuario.EstadoCuenta.Inactivo);

			usuario.setFechaCreacion(LocalDateTime.now());
			usuarioRepository.save(usuario);
			System.out.println("Usuario modificado: " + usuario.getNombre());
		} else {
			System.out.println("Usuario no encontrado");
		}

	}
	@Test void eliminador() {
		Usuario usuario = usuarioRepository.findById(14).orElse(null);
		Assertions.assertThat(usuario).isNotNull();
		if (usuario != null) {
			usuarioRepository.delete(usuario);
			System.out.println("Usuario eliminado: " + usuario.getNombre());
		} else {
			System.out.println("Usuario no encontrado");
		}
	}
	@Test void buscar() {
		Usuario usuario = usuarioRepository.findById(14).orElse(null);
		Assertions.assertThat(usuario).isNotNull();
		if (usuario != null) {
			System.out.println("Usuario encontrado: " + usuario.getNombre());
		} else {
			System.out.println("Usuario no encontrado");
		}
	}
	@Test void listar() {
		Iterable<Usuario> usuarios = usuarioRepository.findAll();
		for (Usuario usuario : usuarios) {
			System.out.println("Usuario: " + usuario.getNombre());
		}
	}

}
