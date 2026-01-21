package co.com.tudominio.facturacionfactus.model.dto.rango;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberingRange implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("document")
    private String document; // Ej: "Factura electrónica de Venta"

    @JsonProperty("prefix")
    private String prefix; // Ej: "SETP"

    @JsonProperty("from")
    private Long from;

    @JsonProperty("to")
    private Long to;

    @JsonProperty("current")
    private Long current;

    @JsonProperty("resolution_number")
    private String resolutionNumber;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("technical_key")
    private String technicalKey;
    
    @JsonProperty("is_active")
    private Integer isActive;
}