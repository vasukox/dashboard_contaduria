package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.dto.common.ReferenceItem;
import co.com.tudominio.facturacionfactus.service.ReferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/referencias")
public class ReferenciaController {

    private final ReferenciaService referenciaService;

    public ReferenciaController(ReferenciaService referenciaService) {
        this.referenciaService = referenciaService;
    }

    @GetMapping("/formas-pago")
    public ResponseEntity<List<ReferenceItem>> listarFormasPago() {
        try {
            return ResponseEntity.ok(referenciaService.listarFormasPago());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/medios-pago")
    public ResponseEntity<List<ReferenceItem>> listarMediosPago() {
        try {
            return ResponseEntity.ok(referenciaService.listarMediosPago());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}