package br.edu.ifsp.horas.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByProntuarioRa(String prontuarioRa);
}