package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.nota.CreditNote;
import co.com.tudominio.facturacionfactus.service.NotaCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/notas-credito")
public class NotaCreditoController {

    private final NotaCreditoService notaService;

    public NotaCreditoController(NotaCreditoService notaService) {
        this.notaService = notaService;
    }

    @PostMapping("/validar")
    public ResponseEntity<BillResponseDto> crearNotaCredito(@RequestBody CreditNote note) {
        try {
            return ResponseEntity.status(201).body(notaService.crearNotaCredito(note));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}