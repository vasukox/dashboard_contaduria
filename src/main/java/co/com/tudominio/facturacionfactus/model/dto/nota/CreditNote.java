package co.com.tudominio.facturacionfactus.model.dto.nota;

import co.com.tudominio.facturacionfactus.model.dto.factura.BillItem;
import co.com.tudominio.facturacionfactus.model.dto.factura.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditNote implements Serializable {

    @JsonProperty("numbering_range_id")
    private String numberingRangeId;

    @JsonProperty("reference_code")
    private String referenceCode;

    @JsonProperty("observation")
    private String observation;

    @JsonProperty("payment_form")
    private String paymentForm;

    @JsonProperty("payment_due_date")
    private String paymentDueDate;

    @JsonProperty("payment_method_code")
    private String paymentMethodCode;

    // Campos nuevos obligatorios para Nota Crédito
    @JsonProperty("bill_id")
    private String billId; // ID interno de la factura en Factus

    @JsonProperty("correction_concept_code")
    private String correctionConceptCode; // "1", "2", "3"...

    // billing_reference a veces es opcional si se envía bill_id, pero lo dejamos por si acaso
    @JsonProperty("billing_reference")
    private BillingReference billingReference;

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("items")
    private List<BillItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingReference implements Serializable {
        @JsonProperty("number")
        private String number;
        @JsonProperty("issue_date")
        private String issueDate;
        @JsonProperty("scheme_name")
        private String schemeName;
        @JsonProperty("scheme_id")
        private String schemeId;
    }
}