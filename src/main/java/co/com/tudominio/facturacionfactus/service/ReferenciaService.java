package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.common.ReferenceItem;
import co.com.tudominio.facturacionfactus.repository.FactusAuthRepository;
import co.com.tudominio.facturacionfactus.repository.FactusReferenceRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReferenciaService {

    private final FactusAuthRepository authRepository;
    private final FactusReferenceRepository referenceRepository;

    public ReferenciaService(FactusAuthRepository authRepository, FactusReferenceRepository referenceRepository) {
        this.authRepository = authRepository;
        this.referenceRepository = referenceRepository;
    }

    private String getToken() throws IOException {
        AuthResponseDto auth = authRepository.login();
        return auth.getAccessToken();
    }

    public List<ReferenceItem> listarFormasPago() throws IOException {
        return referenceRepository.getPaymentForms(getToken()).getData();
    }

    public List<ReferenceItem> listarMediosPago() throws IOException {
        return referenceRepository.getPaymentMethods(getToken()).getData();
    }
}