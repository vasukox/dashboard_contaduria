package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.model.entity.FacturaLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaLocalRepository extends JpaRepository<FacturaLocal, Long> {
    
    // Usamos COALESCE para evitar NULL
    @Query("SELECT COALESCE(SUM(f.total), 0.0) FROM FacturaLocal f")
    Double sumarVentasTotales();

    @Query("SELECT COUNT(f) FROM FacturaLocal f")
    Long contarFacturas();

    List<FacturaLocal> findTop5ByOrderByIssueDateDesc();

    // Consulta nativa para PostgreSQL
    // EXTRACT devuelve double precision, hacemos cast a int
    @Query(value = "SELECT CAST(EXTRACT(MONTH FROM issue_date) AS INTEGER) as mes, COALESCE(SUM(total), 0) as total FROM facturas GROUP BY mes ORDER BY mes", nativeQuery = true)
    List<Object[]> obtenerVentasPorMes();
}