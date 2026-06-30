package br.edu.ifsp.horas.activity.domain.vo;

public record CargaHoraria(Integer minutos) {
    public CargaHoraria {
        if (minutos == null || minutos <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser um valor em minutos maior que zero.");
        }
    }

    public Double emHoras() {
        return minutos / 60.0;
    }
}