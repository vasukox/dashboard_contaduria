package co.com.tudominio.facturacionfactus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso a las páginas principales y la raíz
                .requestMatchers("/", "/login", "/login.html", "/index", "/index.html").permitAll()
                // Permitir acceso a todos los recursos estáticos (CSS, JS, imágenes, etc.)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // Permitir acceso a los endpoints de la API (para login, etc.)
                .requestMatchers("/api/v1/**").permitAll()
                // Cualquier otra petición requiere autenticación (esto lo ajustaremos luego)
                .anyRequest().authenticated()
            );

        return http.build();
    }
}