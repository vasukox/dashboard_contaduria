package co.com.tudominio.facturacionfactus.config;

import io.github.cdimascio.dotenv.Dotenv;


public class FactusConfig {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();



    public static String getApiUrl() {

        return dotenv.get("FACTUS_API_URL", "https://api-sandbox.factus.com.co");
    }

    public static String getClientId() {
        return dotenv.get("FACTUS_CLIENT_ID");
    }

    public static String getClientSecret() {
        return dotenv.get("FACTUS_CLIENT_SECRET");
    }

    public static String getEmail() {
        return dotenv.get("FACTUS_EMAIL");
    }

    public static String getPassword() {
        return dotenv.get("FACTUS_PASSWORD");
    }
}