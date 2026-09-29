package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Kit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KitResponseDTO {
    private Long id;
    private String status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private DoadoraResponseDTO doadora;

    public KitResponseDTO(Kit kit) {
        id = kit.getId();
        status = kit.getStatus();
        criadoEm = kit.getCriadoEm();
        atualizadoEm = kit.getAtualizadoEm();
        doadora = new DoadoraResponseDTO(kit.getDoadora());
    }
}
