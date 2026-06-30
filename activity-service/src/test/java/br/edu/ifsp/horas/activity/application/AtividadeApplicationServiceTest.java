package br.edu.ifsp.horas.activity.application;

import br.edu.ifsp.horas.activity.application.dto.AvaliacaoAtividadeDTO;
import br.edu.ifsp.horas.activity.domain.dao.AtividadeDAO;
import br.edu.ifsp.horas.activity.domain.model.Atividade;
import br.edu.ifsp.horas.activity.domain.vo.CargaHoraria;
import br.edu.ifsp.horas.activity.domain.vo.RaAluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AtividadeApplicationServiceTest {

    private AtividadeDAO atividadeDAOMock;
    private AtividadeApplicationService service;

    @BeforeEach
    void setUp() {
        atividadeDAOMock = mock(AtividadeDAO.class);
        service = new AtividadeApplicationService(atividadeDAOMock);
    }

    @Test
    @DisplayName("Deve deferir uma atividade pendente com sucesso via Service")
    void deveDeferirAtividadeComSucesso() {
        Atividade atividadePendente = new Atividade(new RaAluno("GU203097"), "Semana de Tec", new CargaHoraria(120), LocalDate.now(), "Desc");
        
        when(atividadeDAOMock.buscarPorId(1L)).thenReturn(Optional.of(atividadePendente));

        AvaliacaoAtividadeDTO dto = new AvaliacaoAtividadeDTO("APROVADA", null);
        service.avaliarAtividade(1L, dto);

        assertEquals("APROVADA", atividadePendente.getStatus());
        verify(atividadeDAOMock, times(1)).atualizarStatus(atividadePendente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar indeferir sem justificativa")
    void deveBarrarIndeferimentoSemJustificativa() {
        Atividade atividadePendente = new Atividade(new RaAluno("GU203097"), "Semana de Tec", new CargaHoraria(120), LocalDate.now(), "Desc");
        when(atividadeDAOMock.buscarPorId(1L)).thenReturn(Optional.of(atividadePendente));

        AvaliacaoAtividadeDTO dto = new AvaliacaoAtividadeDTO("REJEITADA", "   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.avaliarAtividade(1L, dto);
        });

        assertTrue(exception.getMessage().contains("justificativa"));
        verify(atividadeDAOMock, never()).atualizarStatus(any());
    }

    @Test
    @DisplayName("Deve excluir uma atividade pendente com sucesso")
    void deveExcluirAtividadePendente() {
        Atividade atividadePendente = new Atividade(new RaAluno("GU203097"), "Semana de Tec", new CargaHoraria(120), LocalDate.now(), "Desc");
        when(atividadeDAOMock.buscarPorId(1L)).thenReturn(Optional.of(atividadePendente));

        // Ação
        service.removerAtividade(1L);

        verify(atividadeDAOMock, times(1)).deletar(1L);
    }

    @Test
    @DisplayName("Não deve permitir excluir uma atividade que já foi avaliada")
    void naoDeveExcluirAtividadeAvaliada() {
        Atividade atividadeAprovada = new Atividade(new RaAluno("GU203097"), "Semana de Tec", new CargaHoraria(120), LocalDate.now(), "Desc");
        atividadeAprovada.deferir(); 
        
        when(atividadeDAOMock.buscarPorId(1L)).thenReturn(Optional.of(atividadeAprovada));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.removerAtividade(1L);
        });

        assertTrue(exception.getMessage().contains("Não é permitido excluir"));
        verify(atividadeDAOMock, never()).deletar(anyLong());
    }
}