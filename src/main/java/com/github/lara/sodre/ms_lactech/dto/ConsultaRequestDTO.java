package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Consulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsultaRequestDTO {

    @NotNull(message = "Campo data é obrigatório")
    @Schema(example = "2026-08-28")
    private LocalDate data;

    @NotBlank(message = "Campo horário é obrigatório")
    @Size(max = 10)
    @Schema(example = "09:00")
    private String horario;

    @NotBlank(message = "Campo motivo é obrigatório")
    @Size(max = 150)
    @Schema(example = "Triagem para se tornar doadora")
    private String motivo;

    @Size(max = 200)
    @Schema(example = "Sem observações")
    private String observacoes;

    @NotBlank(message = "Campo status é obrigatório")
    @Size(max = 20)
    @Schema(example = "AGENDADA")
    private String status;

    @NotNull(message = "Campo doadoraId é obrigatório")
    @Schema(example = "1")
    private Long doadoraId;

    @Schema(example = "1")
    private Long apoioId;

    @NotNull(message = "Campo profissionalId é obrigatório")
    @Schema(example = "1")
    private Long profissionalId;

    public ConsultaRequestDTO(Consulta consulta) {
        data = consulta.getData();
        horario = consulta.getHorario();
        motivo = consulta.getMotivo();
        observacoes = consulta.getObservacoes();
        status = consulta.getStatus();
        doadoraId = consulta.getDoadora().getId();
        apoioId = consulta.getApoio() == null ? null : consulta.getApoio().getId();
        profissionalId = consulta.getProfissional().getId();
    }
}