package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.dto.factura.Bill;
import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import co.com.tudominio.facturacionfactus.model.entity.FacturaLocal;
import co.com.tudominio.facturacionfactus.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping("/validar")
    public ResponseEntity<BillResponseDto> crearFactura(@RequestBody Bill bill) {
        try {
            BillResponseDto response = facturaService.crearFactura(bill);
            return ResponseEntity.status(201).body(response);
        } catch (IOException e) {
            // En un caso real, manejaríamos mejor los errores con un @ControllerAdvice
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FacturaLocal>> obtenerFacturas() {
        return ResponseEntity.ok(facturaService.obtenerTodasLasFacturas());
    }
}