package br.edu.ifsp.horas.activity.presentation;

import br.edu.ifsp.horas.activity.application.AtividadeApplicationService;
import br.edu.ifsp.horas.activity.application.dto.NovaAtividadeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/activities")
public class AtividadeController {

    private final AtividadeApplicationService applicationService;

    public AtividadeController(AtividadeApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> criarAtividade(@RequestBody NovaAtividadeDTO dto) {
        try {
            String tokenProtocolo = applicationService.cadastrarAtividade(dto);
            // Retorna o token em JSON
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("tokenProtocolo", tokenProtocolo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao processar cadastro."));
        }
    }

    @GetMapping("/{tokenProtocolo}")
    public ResponseEntity<?> buscarPorToken(@PathVariable String tokenProtocolo) {
        try {
            return applicationService.buscarPorToken(tokenProtocolo)
                .map(atividade -> ResponseEntity.ok(Map.of(
                    "raAluno", atividade.getRaValor(),
                    "titulo", atividade.getTitulo(),
                    "situacao", atividade.getStatus(),
                    "cargaHorariaHoras", atividade.getCargaHoraria().emHoras(),
                    "descricao", atividade.getDescricao()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Protocolo de atividade não encontrado.")));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/evaluation")
    public ResponseEntity<?> avaliarAtividade(@PathVariable Long id, @RequestBody br.edu.ifsp.horas.activity.application.dto.AvaliacaoAtividadeDTO dto) {
        try {
            applicationService.avaliarAtividade(id, dto);
            return ResponseEntity.ok(Map.of("mensagem", "Avaliação registrada com sucesso."));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable Long id) {
        try {
            applicationService.removerAtividade(id);
            return ResponseEntity.ok(Map.of("mensagem", "Atividade excluída com sucesso."));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", e.getMessage()));
        }
    }

}