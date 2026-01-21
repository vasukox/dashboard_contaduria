package co.com.tudominio.facturacionfactus.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number; // Número de factura (ej: SETP9900001)

    private String cufe;
    
    @Column(name = "public_url")
    private String publicUrl;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    private Double total;

    // Relación con Cliente (Muchas facturas pertenecen a un cliente)
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    // Podríamos guardar los ítems también, pero para estadísticas básicas esto basta por ahora
}