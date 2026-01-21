package co.com.tudominio.facturacionfactus.controller;

import co.com.tudominio.facturacionfactus.model.entity.Cliente;
import co.com.tudominio.facturacionfactus.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{identificacion}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String identificacion) {
        try {
            return ResponseEntity.ok(clienteService.buscarPorIdentificacion(identificacion));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> guardarCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.status(201).body(clienteService.guardarCliente(cliente));
    }

    @DeleteMapping("/{identificacion}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String identificacion) {
        try {
            clienteService.eliminarCliente(identificacion);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}