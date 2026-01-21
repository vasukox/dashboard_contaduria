package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.entity.Usuario;
import co.com.tudominio.facturacionfactus.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // NOTA: En producción, usar BCrypt para comparar contraseñas
            if (usuario.getPassword().equals(password)) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", "fake-jwt-token-" + System.currentTimeMillis()); // Simulación de token
                response.put("user", usuario);
                return ResponseEntity.ok(response);
            }
        }
        
        return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El usuario ya existe"));
        }
        // NOTA: En producción, encriptar contraseña aquí
        usuario.setRol("ADMIN");
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("message", "Usuario creado exitosamente"));
    }
}