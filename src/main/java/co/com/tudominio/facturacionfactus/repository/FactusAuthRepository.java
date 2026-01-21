package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.auth.AuthRequestDto;
import co.com.tudominio.facturacionfactus.model.dto.auth.AuthResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository // Indica a Spring que esta clase maneja acceso a datos/APIs externas
public class FactusAuthRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    // Inyección por constructor (Spring nos pasará el HttpClient y ObjectMapper si los configuramos como Beans,
    // o podemos crearlos aquí si preferimos mantenerlo simple por ahora).
    // Para simplificar la transición, mantendremos la creación interna pero permitiendo inyección futura.
    public FactusAuthRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public AuthResponseDto login() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .grantType("password")
                .clientId(FactusConfig.getClientId())
                .clientSecret(FactusConfig.getClientSecret())
                .username(FactusConfig.getEmail())
                .password(FactusConfig.getPassword())
                .build();

        String jsonBody = objectMapper.writeValueAsString(requestDto);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        String url = FactusConfig.getApiUrl() + "/oauth/token";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la autenticación: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, AuthResponseDto.class);
        }
    }
}