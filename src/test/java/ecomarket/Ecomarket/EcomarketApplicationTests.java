package ecomarket.Ecomarket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecomarket.Ecomarket.DTO.RepartidorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EcomarketApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    @Test
    void contextLoads() {
        // Verifica que la app arranca sin errores
    }

    @Test
    void agregarRepartidorTest() throws Exception {
        RepartidorDTO repartidor = new RepartidorDTO();
        repartidor.setNombre("Pedro Torres");
        repartidor.setTelefono("987654321");
        repartidor.setTipoVehiculo("Furgón");
        repartidor.setPatenteVehiculo("GHJK90");
        repartidor.setTipoVehiculo("Diesel");
        repartidor.setIdProveedor(1); // Asegúrate que este ID existe en la base

        mockMvc.perform(post("/api/repartidores/agregar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(repartidor)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Repartidor agregado")));
    }

    @Test
    void listarRepartidoresTest() throws Exception {
        mockMvc.perform(get("/api/repartidores/listar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void actualizarRepartidorTest() throws Exception {
        RepartidorDTO dto = new RepartidorDTO();
        dto.setNombre("Pedro Actualizado");
        dto.setTelefono("123123123");
        dto.setTipoVehiculo("Camioneta");
        dto.setPatenteVehiculo("ACT123");
        dto.setTipoVehiculo("Gasolina");
        dto.setIdProveedor(1); // Asegúrate que existe

        mockMvc.perform(put("/api/repartidores/actualizar/1") // Usa un ID válido
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("actualizado")));
    }
}
