package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.factura.Customer;
import co.com.tudominio.facturacionfactus.model.entity.Cliente;
import co.com.tudominio.facturacionfactus.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentification(identificacion)
                .orElseThrow(() -> {
                    log.warn("Intento de búsqueda de cliente fallido. ID: {}", identificacion);
                    return new RuntimeException("Cliente no encontrado con cédula: " + identificacion);
                });
    }

    public Cliente guardarCliente(Cliente cliente) {
        log.info("Guardando/Actualizando cliente: {} {}", cliente.getNames(), cliente.getSurname());
        // Verificamos si ya existe para actualizarlo o crearlo
        Optional<Cliente> existente = clienteRepository.findByIdentification(cliente.getIdentification());
        if (existente.isPresent()) {
            log.debug("Cliente ya existe (ID: {}), actualizando registro...", existente.get().getId());
            cliente.setId(existente.get().getId()); // Mantenemos el ID para actualizar
        }
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(String identificacion) {
        log.info("Eliminando cliente con identificación: {}", identificacion);
        Cliente cliente = buscarPorIdentificacion(identificacion);
        clienteRepository.delete(cliente);
    }

    // Metodo utilitario para convertir de Entidad (BD) a DTO
    public Customer convertirADto(Cliente cliente) {
        return Customer.builder()
                .identification(cliente.getIdentification())
                .dv(cliente.getDv())
                .company(cliente.getCompany())
                .names(cliente.getNames())
                .surname(cliente.getSurname())
                .email(cliente.getEmail())
                .phone(cliente.getPhone())
                .address(cliente.getAddress())
                .legalOrganizationId(cliente.getLegalOrganizationId())
                .tributeId(cliente.getTributeId())
                .identificationDocumentId(cliente.getIdentificationDocumentId())
                .municipalityId(cliente.getMunicipalityId())
                .build();
    }
}