package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.dto.rango.NumberingRange;
import co.com.tudominio.facturacionfactus.service.RangoNumeracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rangos")
public class RangoNumeracionController {

    private final RangoNumeracionService rangoService;

    public RangoNumeracionController(RangoNumeracionService rangoService) {
        this.rangoService = rangoService;
    }

    @GetMapping
    public ResponseEntity<List<NumberingRange>> listarRangos() {
        try {
            return ResponseEntity.ok(rangoService.listarRangosActivos());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}