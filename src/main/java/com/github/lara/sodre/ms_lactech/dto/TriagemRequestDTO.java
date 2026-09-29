package com.github.lara.sodre.ms_lactech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter
public class TriagemRequestDTO {
    @NotNull private Long doadoraId;
    @NotBlank @Size(max=20) private String amamentando;
    @NotBlank @Size(max=50) private String idadeBebe;
    @NotBlank @Size(max=20) private String medicamento;
    @NotBlank @Size(max=20) private String fuma;
    @NotBlank @Size(max=20) private String examesRecentes;
    @NotBlank @Size(max=20) private String jaDoou;
}
