package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.common.ReferenceResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusReferenceRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusReferenceRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public ReferenceResponseDto getPaymentForms(String token) throws IOException {
        return executeRequest("/v1/payment-forms", token);
    }

    public ReferenceResponseDto getPaymentMethods(String token) throws IOException {
        // A veces Factus requiere filtrar por nombre o trae todos. Probemos traer todos.
        return executeRequest("/v1/payment-methods?filter[name]=", token);
    }

    private ReferenceResponseDto executeRequest(String endpoint, String token) throws IOException {
        String url = FactusConfig.getApiUrl() + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo referencias: " + response.code());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, ReferenceResponseDto.class);
        }
    }
}