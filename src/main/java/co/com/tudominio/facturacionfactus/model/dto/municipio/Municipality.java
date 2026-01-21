package co.com.tudominio.facturacionfactus.model.dto.municipio;

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
public class Municipality implements Serializable {

    @JsonProperty("id")
    private String id; // Factus usa IDs numéricos pero a veces conviene String para selects

    @JsonProperty("code")
    private String code; // Código DANE (ej: 11001)

    @JsonProperty("name")
    private String name; // Ej: Bogotá, D.C.

    @JsonProperty("department")
    private String department; // Ej: Bogotá, D.C.
}