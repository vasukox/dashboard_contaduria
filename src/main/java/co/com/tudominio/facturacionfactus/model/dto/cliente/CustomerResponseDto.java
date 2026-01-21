package co.com.tudominio.facturacionfactus.model.dto.cliente;

import co.com.tudominio.facturacionfactus.model.dto.factura.Customer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResponseDto implements Serializable {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private CustomerData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerData implements Serializable {
        // Dependiendo del endpoint (crear vs listar), la data puede ser un objeto o una lista.
        // Para 'crear', suele devolver el objeto creado.
        // Para 'listar', devuelve una lista y paginación.
        
        @JsonProperty("customer")
        private CustomerDetails customer;
        
        @JsonProperty("data") // En listados, Factus suele devolver 'data' dentro de 'data'
        private List<CustomerDetails> customersList;
        
        @JsonProperty("pagination")
        private Pagination pagination;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerDetails extends Customer {
        // Heredamos de Customer para tener los campos base, y agregamos los que devuelve la API
        @JsonProperty("id")
        private Long id;
        
        @JsonProperty("created_at")
        private String createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagination implements Serializable {
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("per_page")
        private Integer perPage;
        @JsonProperty("current_page")
        private Integer currentPage;
        @JsonProperty("last_page")
        private Integer lastPage;
    }
}