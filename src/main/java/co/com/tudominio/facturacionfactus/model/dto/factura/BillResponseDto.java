package co.com.tudominio.facturacionfactus.model.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos extra que no mapeemos
public class BillResponseDto implements Serializable {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private BillData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillData implements Serializable {
        @JsonProperty("bill")
        private BillDetails bill;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillDetails implements Serializable {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("number")
        private String number;

        @JsonProperty("qr")
        private String qr;

        @JsonProperty("cufe")
        private String cufe;
        
        @JsonProperty("public_url")
        private String publicUrl;
    }
}