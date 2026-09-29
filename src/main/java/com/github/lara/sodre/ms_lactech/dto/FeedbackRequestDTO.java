package com.github.lara.sodre.ms_lactech.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackRequestDTO {

    @NotNull(message = "Campo coletaId é obrigatório")
    private Long coletaId;

    @NotNull(message = "Campo nota é obrigatório")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;
}
