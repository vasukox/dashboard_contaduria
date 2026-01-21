package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.municipio.Municipality;
import co.com.tudominio.facturacionfactus.model.dto.municipio.MunicipalityResponseDto;
import co.com.tudominio.facturacionfactus.repository.FactusAuthRepository;
import co.com.tudominio.facturacionfactus.repository.FactusMunicipalityRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MunicipioService {

    private final FactusAuthRepository authRepository;
    private final FactusMunicipalityRepository municipalityRepository;

    public MunicipioService(FactusAuthRepository authRepository, FactusMunicipalityRepository municipalityRepository) {
        this.authRepository = authRepository;
        this.municipalityRepository = municipalityRepository;
    }

    public List<Municipality> buscarMunicipios(String nombre) throws IOException {
        AuthResponseDto auth = authRepository.login();
        MunicipalityResponseDto response = municipalityRepository.buscarMunicipios(nombre, auth.getAccessToken());
        return response.getData();
    }
}