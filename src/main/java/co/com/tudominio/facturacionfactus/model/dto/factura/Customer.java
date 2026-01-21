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
public class Customer implements Serializable {

    @JsonProperty("identification")
    private String identification;

    @JsonProperty("dv")
    private String dv; // Dígito de verificación (para NITs)

    @JsonProperty("company")
    private String company; // Razón social (si es empresa)

    @JsonProperty("names")
    private String names; // Nombres (si es persona)

    @JsonProperty("surname")
    private String surname; // Apellidos

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("legal_organization_id")
    private String legalOrganizationId; // "1" Persona Jurídica, "2" Persona Natural

    @JsonProperty("tribute_id")
    private String tributeId; // "21" para No responsables de IVA, "01" para IVA, etc.

    @JsonProperty("identification_document_id")
    private String identificationDocumentId; // "3" Cédula, "6" NIT, etc.

    @JsonProperty("municipality_id")
    private String municipalityId; // Código del municipio según DANE
}