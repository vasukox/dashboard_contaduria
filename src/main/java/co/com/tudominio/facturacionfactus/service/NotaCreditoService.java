package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.nota.CreditNote;
import co.com.tudominio.facturacionfactus.repository.FactusAuthRepository;
import co.com.tudominio.facturacionfactus.repository.FactusCreditNoteRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotaCreditoService {

    private final FactusAuthRepository authRepository;
    private final FactusCreditNoteRepository creditNoteRepository;

    public NotaCreditoService(FactusAuthRepository authRepository, FactusCreditNoteRepository creditNoteRepository) {
        this.authRepository = authRepository;
        this.creditNoteRepository = creditNoteRepository;
    }

    public BillResponseDto crearNotaCredito(CreditNote note) throws IOException {
        AuthResponseDto auth = authRepository.login();
        return creditNoteRepository.validateCreditNote(note, auth.getAccessToken());
    }
}