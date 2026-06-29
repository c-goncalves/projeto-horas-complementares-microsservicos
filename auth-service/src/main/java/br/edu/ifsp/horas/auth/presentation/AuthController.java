package br.edu.ifsp.horas.auth.presentation;

import br.edu.ifsp.horas.auth.application.AuthApplicationService;
import br.edu.ifsp.horas.auth.domain.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthApplicationService authService;

    public AuthController(AuthApplicationService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody Usuario usuario) {
        authService.cadastrar(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credenciais) {
        String token = authService.login(credenciais.get("prontuario"), credenciais.get("senha"));
        return ResponseEntity.ok(Map.of("token", token));
    }
}