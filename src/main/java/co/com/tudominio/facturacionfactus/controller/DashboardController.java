package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.entity.FacturaLocal;
import co.com.tudominio.facturacionfactus.repository.ClienteRepository;
import co.com.tudominio.facturacionfactus.repository.FacturaLocalRepository;
import co.com.tudominio.facturacionfactus.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final FacturaLocalRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public DashboardController(FacturaLocalRepository facturaRepository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        try {
            Double totalVentas = facturaRepository.sumarVentasTotales();
            Long totalFacturas = facturaRepository.contarFacturas();
            Long totalClientes = clienteRepository.count();
            Long totalProductos = productoRepository.count();

            List<FacturaLocal> ultimasFacturas = facturaRepository.findTop5ByOrderByIssueDateDesc();
            if (ultimasFacturas == null) ultimasFacturas = Collections.emptyList();

            // SIMPLIFICACIÓN: Enviamos lista vacía para aislar el error de la consulta SQL compleja
            List<VentaMes> ventasMes = new ArrayList<>();
            // ventasMes.add(new VentaMes(1, 100000.0)); // Datos de prueba si quieres ver el gráfico

            return ResponseEntity.ok(new DashboardStats(
                    totalVentas != null ? totalVentas : 0.0,
                    totalFacturas != null ? totalFacturas : 0L,
                    totalClientes,
                    totalProductos,
                    ultimasFacturas,
                    ventasMes
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new DashboardStats(0.0, 0L, 0L, 0L, Collections.emptyList(), Collections.emptyList()));
        }
    }

    @Data
    @AllArgsConstructor
    public static class DashboardStats {
        private Double totalVentas;
        private Long totalFacturas;
        private Long totalClientes;
        private Long totalProductos;
        private List<FacturaLocal> ultimasFacturas;
        private List<VentaMes> ventasPorMes;
    }

    @Data
    @AllArgsConstructor
    public static class VentaMes {
        private Integer mes;
        private Double total;
    }
}