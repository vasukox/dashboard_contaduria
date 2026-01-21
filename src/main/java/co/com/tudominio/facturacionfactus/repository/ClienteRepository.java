package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método mágico: Spring crea el SQL "SELECT * FROM clientes WHERE identification = ?"
    Optional<Cliente> findByIdentification(String identification);
}