package br.edu.ifsp.horas.activity.presentation;

import br.edu.ifsp.horas.activity.application.AtividadeApplicationService;
import br.edu.ifsp.horas.activity.application.dto.NovaAtividadeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtividadeTest {

    @Mock
    private AtividadeApplicationService applicationService;

    @InjectMocks
    private AtividadeController atividadeController;

    @Test
    @DisplayName("Deve retornar código 201 CREATED e o token de protocolo ao cadastrar com sucesso")
    void deveCadastrarAtividadeComSucesso() {
        NovaAtividadeDTO dto = new NovaAtividadeDTO(
                "EXT001",
                "Curso Avançado",
                1200,
                LocalDate.now(),
                "SP123456"
        );

        when(applicationService.cadastrarAtividade(dto)).thenReturn("656715");

        ResponseEntity<?> resposta = atividadeController.criar(dto);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Map<?, ?> body = (Map<?, ?>) resposta.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("tokenProtocolo")).isEqualTo("656715");
    }
}
