package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Avaliacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AvaliacaoRequestDTO {

    @NotNull(message = "Campo data é obrigatório")
    @Schema(example = "2026-08-28")
    private LocalDate data;

    @NotNull(message = "Campo nota é obrigatório")
    @Min(value = 0, message = "A nota mínima é 0")
    @Max(value = 5, message = "A nota máxima é 5")
    @Schema(example = "5")
    private BigDecimal nota;

    @Size(max = 500)
    @Schema(example = "Atendimento excelente! Equipe muito atenciosa.")
    private String comentario;

    @Schema(example = "1")
    private Long doadoraId;

    @Schema(example = "1")
    private Long apoioId;

    @NotNull(message = "Campo profissionalId é obrigatório")
    @Schema(example = "1")
    private Long profissionalId;

    @NotNull(message = "Campo consultaId é obrigatório")
    @Schema(example = "1")
    private Long consultaId;

    public AvaliacaoRequestDTO(Avaliacao avaliacao) {
        data = avaliacao.getData();
        nota = avaliacao.getNota();
        comentario = avaliacao.getComentario();
        doadoraId = avaliacao.getDoadora() == null ? null : avaliacao.getDoadora().getId();
        apoioId = avaliacao.getApoio() == null ? null : avaliacao.getApoio().getId();
        profissionalId = avaliacao.getProfissional().getId();
        consultaId = avaliacao.getConsulta().getId();
    }

}