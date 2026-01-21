package co.com.tudominio.facturacionfactus.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // Genera Getters, Setters, toString, etc. automaticamente
@Builder // Nos permite construir el objeto fácilmente
@NoArgsConstructor // Constructor vacío (útil para Jackson y frameworks)
@AllArgsConstructor // Constructor con todos los argumentos (necesario para @Builder)
public class AuthRequestDto implements Serializable {

    // Mapeamos el nombre Java al nombre exacto que pide la API (snake_case)
    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}