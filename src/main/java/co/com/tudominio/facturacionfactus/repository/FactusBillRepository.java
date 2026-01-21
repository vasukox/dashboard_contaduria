package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.factura.Bill;
import co.com.tudominio.facturacionfactus.model.dto.factura.BillResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusBillRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusBillRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public BillResponseDto validateBill(Bill bill, String token) throws IOException {
        String jsonBody = objectMapper.writeValueAsString(bill);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        String url = FactusConfig.getApiUrl() + "/v1/bills/validate";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error enviando factura: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, BillResponseDto.class);
        }
    }
}