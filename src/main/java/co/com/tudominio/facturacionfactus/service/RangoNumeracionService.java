package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.rango.NumberingRange;
import co.com.tudominio.facturacionfactus.model.dto.rango.NumberingRangeResponseDto;
import co.com.tudominio.facturacionfactus.repository.FactusAuthRepository;
import co.com.tudominio.facturacionfactus.repository.FactusRangeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RangoNumeracionService {

    private final FactusAuthRepository authRepository;
    private final FactusRangeRepository rangeRepository;

    public RangoNumeracionService(FactusAuthRepository authRepository, FactusRangeRepository rangeRepository) {
        this.authRepository = authRepository;
        this.rangeRepository = rangeRepository;
    }

    public List<NumberingRange> listarRangosActivos() throws IOException {
        // 1. Obtener Token
        AuthResponseDto auth = authRepository.login();
        String token = auth.getAccessToken();

        // 2. Consultar Rangos
        NumberingRangeResponseDto response = rangeRepository.getNumberingRanges(token);
        
        // Accedemos a data -> ranges
        return response.getData().getRanges();
    }
}