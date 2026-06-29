package br.edu.ifsp.horas.activity.domain.dao;

import br.edu.ifsp.horas.activity.domain.model.Atividade;
import java.util.Optional;

public interface AtividadeDAO {
    void salvar(Atividade atividade);
    Optional<Atividade> buscarPorToken(String codigoAcesso);
    
    Optional<Atividade> buscarPorId(Long id);
    void atualizarStatus(Atividade atividade);
    void deletar(Long id);
}