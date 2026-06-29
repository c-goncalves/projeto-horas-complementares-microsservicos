package br.edu.ifsp.horas.activity.application;

import br.edu.ifsp.horas.activity.application.dto.NovaAtividadeDTO;
import br.edu.ifsp.horas.activity.domain.dao.AtividadeDAO;
import br.edu.ifsp.horas.activity.domain.model.Atividade;
import br.edu.ifsp.horas.activity.domain.vo.CargaHoraria;
import br.edu.ifsp.horas.activity.domain.vo.RaAluno;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AtividadeApplicationService {

    private final AtividadeDAO atividadeDAO;

    public AtividadeApplicationService(AtividadeDAO atividadeDAO) {
        this.atividadeDAO = atividadeDAO;
    }

    @Transactional
    public String cadastrarAtividade(NovaAtividadeDTO dto) {
        RaAluno ra = new RaAluno(dto.raAluno());
        CargaHoraria carga = new CargaHoraria(dto.cargaHorariaMinutos());
        
        Atividade novaAtividade = new Atividade(
            ra,
            dto.titulo(),
            carga,
            dto.dataRealizacao(),
            dto.descricao()
        );
        
        novaAtividade.anexarComprovante("arquivos/comprovantes/mock-certificado.pdf");
        atividadeDAO.salvar(novaAtividade);

        return novaAtividade.getCodigoAcesso();
    }

    public Optional<Atividade> buscarPorToken(String codigoAcesso) {
        return atividadeDAO.buscarPorToken(codigoAcesso);
    }

    @Transactional
    public void avaliarAtividade(Long id, br.edu.ifsp.horas.activity.application.dto.AvaliacaoAtividadeDTO dto) {
        Atividade atividade = atividadeDAO.buscarPorId(id)
            .orElseThrow(() -> new java.util.NoSuchElementException("Atividade não encontrada."));

        if ("APROVADA".equalsIgnoreCase(dto.status())) {
            atividade.deferir();
        } else if ("REJEITADA".equalsIgnoreCase(dto.status())) {
            atividade.indeferir(dto.justificativa());
        } else {
            throw new IllegalArgumentException("Status de avaliação inválido. Use APROVADA ou REJEITADA.");
        }

        atividadeDAO.atualizarStatus(atividade);
    }

    @Transactional
    public void removerAtividade(Long id) {
        Atividade atividade = atividadeDAO.buscarPorId(id)
            .orElseThrow(() -> new java.util.NoSuchElementException("Atividade não encontrada."));
            
        if (!"PENDENTE".equals(atividade.getStatus())) {
            throw new IllegalStateException("Não é permitido excluir uma atividade que já foi avaliada.");
        }
        
        atividadeDAO.deletar(id);
    }
}