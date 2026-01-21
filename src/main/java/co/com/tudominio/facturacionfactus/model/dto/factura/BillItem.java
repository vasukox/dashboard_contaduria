package co.com.tudominio.facturacionfactus.model.dto.factura;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillItem implements Serializable {

    @JsonProperty("code_reference")
    private String codeReference; // Tu código interno del producto

    @JsonProperty("name")
    private String name; // Nombre del producto

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("discount_rate")
    private Double discountRate; // Porcentaje de descuento (0 a 100)

    @JsonProperty("price")
    private Double price; // Precio unitario

    @JsonProperty("tax_rate")
    private String taxRate; // "19.00" (String porque a veces la API lo pide así o decimal)

    @JsonProperty("unit_measure_id")
    private String unitMeasureId; // "70" es unidad estándar. Ver tablas maestras.

    @JsonProperty("standard_code_id")
    private String standardCodeId; // "1" Estándar de adopción del contribuyente

    @JsonProperty("is_excluded")
    private Integer isExcluded; // 0 o 1 (Si está excluido de IVA)

    @JsonProperty("tribute_id")
    private String tributeId; // "1" para IVA

    @JsonProperty("withholding_taxes")
    private java.util.List<Object> withholdingTaxes; // Retenciones (lista vacía si no hay)
}