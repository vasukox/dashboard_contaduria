package co.com.tudominio.facturacionfactus.model.dto.factura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // No envía campos que sean null
public class Bill implements Serializable {

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

    @JsonProperty("billing_period")
    private Object billingPeriod; // Cambiado a Object para poder enviarlo null o con estructura

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("items")
    private List<BillItem> items;
}