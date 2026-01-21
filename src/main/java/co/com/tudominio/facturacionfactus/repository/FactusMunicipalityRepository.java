package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.municipio.MunicipalityResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusMunicipalityRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusMunicipalityRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public MunicipalityResponseDto buscarMunicipios(String nombre, String token) throws IOException {
        String url = FactusConfig.getApiUrl() + "/v1/municipalities?filter[name]=" + nombre;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error buscando municipios: " + response.code());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, MunicipalityResponseDto.class);
        }
    }
}