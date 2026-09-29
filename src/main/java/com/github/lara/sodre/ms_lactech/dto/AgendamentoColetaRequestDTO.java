package com.github.lara.sodre.ms_lactech.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@AllArgsConstructor @NoArgsConstructor @Getter
public class AgendamentoColetaRequestDTO {
 @NotNull private Long doadoraId; @NotNull private LocalDate data; @NotBlank @Size(max=20) private String turno; @Size(max=200) private String observacoes;
}
