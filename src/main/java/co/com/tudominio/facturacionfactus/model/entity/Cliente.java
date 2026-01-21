package co.com.tudominio.facturacionfactus.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("identification")
    private String identification;

    @JsonProperty("dv")
    private String dv;

    @JsonProperty("company")
    private String company;

    @JsonProperty("names")
    private String names;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;
    
    @Column(name = "legal_organization_id")
    @JsonProperty("legal_organization_id")
    private String legalOrganizationId;
    
    @Column(name = "tribute_id")
    @JsonProperty("tribute_id")
    private String tributeId;
    
    @Column(name = "identification_document_id")
    @JsonProperty("identification_document_id")
    private String identificationDocumentId;

    
    @Column(name = "municipality_id")
    @JsonProperty("municipality_id")
    private String municipalityId;
}