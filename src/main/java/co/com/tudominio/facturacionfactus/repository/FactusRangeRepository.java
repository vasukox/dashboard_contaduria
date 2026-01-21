package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.rango.NumberingRangeResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusRangeRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusRangeRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public NumberingRangeResponseDto getNumberingRanges(String token) throws IOException {
        String url = FactusConfig.getApiUrl() + "/v1/numbering-ranges?filter[is_active]=1"; // Solo activos

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo rangos: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, NumberingRangeResponseDto.class);
        }
    }
}