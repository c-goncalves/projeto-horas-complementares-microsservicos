package br.edu.ifsp.horas.auth.application;

import br.edu.ifsp.horas.auth.domain.Usuario;
import br.edu.ifsp.horas.auth.domain.UsuarioRepository;
import br.edu.ifsp.horas.auth.infrastructure.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthApplicationService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthApplicationService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void cadastrar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.save(usuario);
    }

    public String login(String prontuario, String senhaPura) {
        Usuario usuario = repository.findByProntuarioRa(prontuario)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(senhaPura, usuario.getSenha())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        return jwtService.gerarToken(usuario.getProntuarioRa(), usuario.getRole());
    }
}