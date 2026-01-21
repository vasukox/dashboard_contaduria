package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodeReference(String codeReference);
}