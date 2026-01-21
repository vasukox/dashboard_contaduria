package co.com.tudominio.facturacionfactus.repository;

import co.com.tudominio.facturacionfactus.config.FactusConfig;
import co.com.tudominio.facturacionfactus.model.dto.cliente.CustomerResponseDto;
import co.com.tudominio.facturacionfactus.model.dto.factura.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FactusCustomerRepository {

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FactusCustomerRepository(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public CustomerResponseDto getCustomers(String token) throws IOException {
        String url = FactusConfig.getApiUrl() + "/v1/customers";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return executeRequest(request);
    }

    public CustomerResponseDto getCustomerById(String identification, String token) throws IOException {
        // Factus permite buscar por identificación
        String url = FactusConfig.getApiUrl() + "/v1/customers?filter[identification]=" + identification;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return executeRequest(request);
    }

    public CustomerResponseDto createCustomer(Customer customer, String token) throws IOException {
        String url = FactusConfig.getApiUrl() + "/v1/customers";
        String jsonBody = objectMapper.writeValueAsString(customer);
        
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return executeRequest(request);
    }

    // Método privado para reutilizar la lógica de ejecución y parseo
    private CustomerResponseDto executeRequest(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en API Clientes: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, CustomerResponseDto.class);
        }
    }
}