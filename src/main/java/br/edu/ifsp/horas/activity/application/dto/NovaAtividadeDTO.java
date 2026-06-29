package br.edu.ifsp.horas.activity.application.dto;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record NovaAtividadeDTO(
    String raAluno,
    String titulo,
    Integer cargaHorariaMinutos,
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataRealizacao,
    String descricao
) {}