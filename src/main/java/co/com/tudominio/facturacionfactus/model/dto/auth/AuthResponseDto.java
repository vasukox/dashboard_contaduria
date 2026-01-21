package co.com.tudominio.facturacionfactus.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor // Constructor vacío necesario para Jackson
@AllArgsConstructor
public class AuthResponseDto implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("scope")
    private String scope;

    // Campo opcional para capturar errores si la API devuelve uno
    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}