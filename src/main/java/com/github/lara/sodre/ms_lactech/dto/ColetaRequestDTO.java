package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Coleta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ColetaRequestDTO {

    @NotNull(message = "Campo data é obrigatório")
    @Schema(example = "2026-08-20")
    private LocalDate data;

    @Positive(message = "O campo volume deve ser positivo quando informado")
    @Schema(example = "180")
    private BigDecimal volume;

    @NotBlank(message = "Campo turno é obrigatório")
    @Size(max = 20)
    private String turno;

    @NotBlank(message = "Campo status é obrigatório")
    @Size(max = 20, message = "O campo status deve ter no máximo 20 caracteres")
    @Schema(example = "AGENDADA")
    private String status;

    @Size(max = 200, message = "O campo observações deve ter no máximo 200 caracteres")
    @Schema(example = "Melhor horario: manhã")
    private String observacoes;

    @NotNull(message = "Campo doadoraId é obrigatório")
    @Schema(example = "1")
    private Long doadoraId;

    public ColetaRequestDTO(Coleta coleta) {
        data = coleta.getData();
        volume = coleta.getVolume();
        status = coleta.getStatus();
        turno = coleta.getTurno();
        observacoes = coleta.getObservacoes();
        doadoraId = coleta.getDoadora().getId();
    }

}