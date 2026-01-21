package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.dto.municipio.Municipality;
import co.com.tudominio.facturacionfactus.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/municipios")
public class MunicipioController {

    private final MunicipioService municipioService;

    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @GetMapping
    public ResponseEntity<List<Municipality>> buscarMunicipios(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(municipioService.buscarMunicipios(nombre));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}