package com.github.lara.sodre.ms_lactech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter
public class TriagemStatusRequestDTO {
    @NotBlank
    @Pattern(regexp="PENDENTE|EM_ANALISE|APROVADA|NAO_APROVADA")
    private String status;
}
