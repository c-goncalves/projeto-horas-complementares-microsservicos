package br.edu.ifsp.horas.activity.domain.model;

import br.edu.ifsp.horas.activity.domain.vo.CargaHoraria;
import br.edu.ifsp.horas.activity.domain.vo.RaAluno;
import java.time.LocalDate;
import java.util.Random;

public class Atividade {
    private Long id;
    private String codigoAcesso; 
    private RaAluno raAluno;   
    private String titulo;
    private CargaHoraria cargaHoraria;
    private LocalDate dataRealizacao;
    private String descricao;
    private String status; // PENDENTE, APROVADA, REJEITADA
    private String pathComprovante;
    private String justificativa;

    public Atividade(RaAluno raAluno, String titulo, CargaHoraria cargaHoraria, LocalDate dataRealizacao, String descricao) {
        this.codigoAcesso = gerarCodigoAleatorio();
        this.raAluno = raAluno;
        this.titulo = titulo;
        this.cargaHoraria = cargaHoraria;
        this.dataRealizacao = dataRealizacao;
        this.descricao = descricao;
        this.status = "PENDENTE";
    }

    private String gerarCodigoAleatorio() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000);
        return String.valueOf(numero);
    }

    public void anexarComprovante(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Caminho do comprovante inválido.");
        }
        this.pathComprovante = path;
    }

    public void deferir() {
        if (!"PENDENTE".equals(this.status)) {
            throw new IllegalStateException("Esta atividade já foi avaliada e não pode ser alterada.");
        }
        this.status = "APROVADA";
    }

    public void indeferir(String justificativa) {
        if (!"PENDENTE".equals(this.status)) {
            throw new IllegalStateException("Esta atividade já foi avaliada e não pode ser alterada.");
        }
        if (justificativa == null || justificativa.strip().isEmpty()) {
            throw new IllegalArgumentException("É obrigatório informar uma justificativa para indeferir a atividade.");
        }
        this.status = "REJEITADA";
        this.justificativa = justificativa;
    }

    public void reconstruirStatus(String status, String justificativa) {
        this.status = status;
        this.justificativa = justificativa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigoAcesso() { return codigoAcesso; }
    public String getRaValor() { return raAluno.valor(); } 
    public String getTitulo() { return titulo; }
    public CargaHoraria getCargaHoraria() { return cargaHoraria; }
    public LocalDate getDataRealizacao() { return dataRealizacao; }
    public String getDescricao() { return descricao; }
    public String getStatus() { return status; }
    public String getPathComprovante() { return pathComprovante; }
    public String getJustificativa() { return justificativa; }
}