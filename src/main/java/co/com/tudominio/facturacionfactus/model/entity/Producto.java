package co.com.tudominio.facturacionfactus.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "code_reference")
    @JsonProperty("code_reference")
    private String codeReference; // Tu código interno (ej: PROD-001)

    @Column(nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("price")
    private Double price;

    @Column(name = "tax_rate")
    @JsonProperty("tax_rate")
    private String taxRate; // Ej: "19.00"

    @Column(name = "unit_measure_id")
    @JsonProperty("unit_measure_id")
    private String unitMeasureId; // Ej: "70" (Unidad)

    @Column(name = "standard_code_id")
    @JsonProperty("standard_code_id")
    private String standardCodeId; // Ej: "1"

    @Column(name = "is_excluded")
    @JsonProperty("is_excluded")
    private Integer isExcluded; // 0 o 1

    @Column(name = "tribute_id")
    @JsonProperty("tribute_id")
    private String tributeId; // Ej: "1" (IVA)
}