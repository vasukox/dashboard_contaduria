package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.nota.CreditNote;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusCreditNoteRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusCreditNoteRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public BillResponseDto validateCreditNote(CreditNote note, String token) throws IOException {
        String jsonBody = objectMapper.writeValueAsString(note);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        
        // Endpoint específico para Notas Crédito
        String url = FactusConfig.getApiUrl() + "/v1/credit-notes/validate";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error enviando Nota Crédito: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            // La respuesta suele tener la misma estructura que una factura
            return objectMapper.readValue(responseBody, BillResponseDto.class);
        }
    }
}