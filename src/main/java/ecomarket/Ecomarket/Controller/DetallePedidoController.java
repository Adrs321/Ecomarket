package ecomarket.Ecomarket.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecomarket.Ecomarket.Model.DetallePedido;
import ecomarket.Ecomarket.Model.Pedido;
import ecomarket.Ecomarket.Model.Producto;
import ecomarket.Ecomarket.Repositorio.DetallePedidoRepository;
import ecomarket.Ecomarket.Repositorio.PedidoRepository;
import ecomarket.Ecomarket.Repositorio.ProductoRepository;



@RestController
@RequestMapping("/api/detallePedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

     ////Crear un detalle de pedido
    @PostMapping("/grabar")
   public DetallePedido crearDetallePedido(@RequestBody DetallePedido detallePedido) {
         ////Buscar y asignar el producto
        if (detallePedido.getProducto() != null && detallePedido.getProducto().getId() != null) {
            Producto producto = productoRepository.findById(detallePedido.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            detallePedido.setProducto(producto);
        } else {
           throw new RuntimeException("Debe indicar el id del producto");
       }

         ////Buscar y asignar el pedido
       if (detallePedido.getPedido() != null && detallePedido.getPedido().getId() != null) {
            Pedido pedido = pedidoRepository.findById(detallePedido.getPedido().getId())
               .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
            detallePedido.setPedido(pedido);
        } else {
           throw new RuntimeException("Debe indicar el id del pedido");
        }
       return detallePedidoRepository.save(detallePedido);
   }
    ////Listar todos los detalles de pedidos
    @GetMapping("/listar")
    public List<DetallePedido> listarDetallesPedidos() {
        return detallePedidoRepository.findAll();
    }
    // Actualizar un detalle de pedido
    @PutMapping("/actualizar/{id}")
    public DetallePedido actualizarDetallePedido(@PathVariable Long id, @RequestBody DetallePedido detallePedidoActualizado) {
        return detallePedidoRepository.findById(id).map(detallePedido -> {
            detallePedido.setCantidad(detallePedidoActualizado.getCantidad());
            detallePedido.setPrecioUnitario(detallePedidoActualizado.getPrecioUnitario());
            detallePedido.setProducto(detallePedidoActualizado.getProducto());
            return detallePedidoRepository.save(detallePedido);
        }).orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado"));
    }
}
