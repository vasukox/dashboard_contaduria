package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.factura.Bill;
import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.factura.Customer;
import co.com.tudominio.facturacionfactus.model.entity.Cliente;
import co.com.tudominio.facturacionfactus.model.entity.FacturaLocal;
import co.com.tudominio.facturacionfactus.repository.ClienteRepository;
import co.com.tudominio.facturacionfactus.repository.FacturaLocalRepository;
import co.com.tudominio.facturacionfactus.repository.FactusAuthRepository;
import co.com.tudominio.facturacionfactus.repository.FactusBillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FacturaService {

    private final FactusAuthRepository authRepository;
    private final FactusBillRepository billRepository;
    private final FacturaLocalRepository facturaLocalRepository;
    private final ClienteRepository clienteRepository;

    public FacturaService(FactusAuthRepository authRepository, FactusBillRepository billRepository, 
                          FacturaLocalRepository facturaLocalRepository, ClienteRepository clienteRepository) {
        this.authRepository = authRepository;
        this.billRepository = billRepository;
        this.facturaLocalRepository = facturaLocalRepository;
        this.clienteRepository = clienteRepository;
    }

    public BillResponseDto crearFactura(Bill bill) throws IOException {
        log.info("Iniciando proceso de creación de factura para cliente: {}", bill.getCustomer().getIdentification());
        
        // 1. Login
        log.debug("Autenticando con API Factus...");
        AuthResponseDto authResponse = authRepository.login();
        String token = authResponse.getAccessToken();

        // 2. Enviar a Factus
        log.debug("Enviando factura a Factus...");
        BillResponseDto response = billRepository.validateBill(bill, token);

        // 3. Guardar en BD Local
        if (response.getData() != null && response.getData().getBill() != null) {
            log.info("Factura validada exitosamente por Factus. Número: {}", response.getData().getBill().getNumber());
            guardarFacturaLocal(bill, response);
        } else {
            log.error("Respuesta de Factus incompleta o inválida: {}", response);
        }

        return response;
    }

    private void guardarFacturaLocal(Bill bill, BillResponseDto response) {
        try {
            double total = bill.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();

            // Verificar si el cliente existe, si no, crearlo para mantener la integridad
            Cliente cliente = obtenerOGuardarCliente(bill.getCustomer());

            FacturaLocal facturaLocal = FacturaLocal.builder()
                    .number(response.getData().getBill().getNumber())
                    .cufe(response.getData().getBill().getCufe())
                    .publicUrl(response.getData().getBill().getPublicUrl())
                    .issueDate(LocalDateTime.now())
                    .total(total)
                    .cliente(cliente)
                    .build();

            facturaLocalRepository.save(facturaLocal);
            log.info("Factura guardada en base de datos local con ID: {}", facturaLocal.getId());
        } catch (Exception e) {
            log.error("Error CRÍTICO al guardar factura local: {}", e.getMessage(), e);
        }
    }

    private Cliente obtenerOGuardarCliente(Customer customerDto) {
        return clienteRepository.findByIdentification(customerDto.getIdentification())
                .orElseGet(() -> {
                    log.info("Cliente nuevo detectado en factura. Creando cliente: {}", customerDto.getIdentification());
                    Cliente nuevoCliente = Cliente.builder()
                            .identification(customerDto.getIdentification())
                            .dv(customerDto.getDv())
                            .company(customerDto.getCompany())
                            .names(customerDto.getNames())
                            .surname(customerDto.getSurname())
                            .email(customerDto.getEmail())
                            .phone(customerDto.getPhone())
                            .address(customerDto.getAddress())
                            .legalOrganizationId(customerDto.getLegalOrganizationId())
                            .tributeId(customerDto.getTributeId())
                            .identificationDocumentId(customerDto.getIdentificationDocumentId())
                            .municipalityId(customerDto.getMunicipalityId())
                            .build();
                    return clienteRepository.save(nuevoCliente);
                });
    }

    public List<FacturaLocal> obtenerTodasLasFacturas() {
        log.debug("Consultando historial de facturas");
        return facturaLocalRepository.findAll();
    }
}