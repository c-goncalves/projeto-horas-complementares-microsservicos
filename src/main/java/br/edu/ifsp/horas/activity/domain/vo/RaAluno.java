package br.edu.ifsp.horas.activity.domain.vo;

public record RaAluno(String valor) {
    public RaAluno {
        if (valor == null || !valor.matches("^GU\\d{6}$")) {
            throw new IllegalArgumentException("RA inválido.(ex: GU203097).");
        }
    }
}