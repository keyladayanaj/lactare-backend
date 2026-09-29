package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Coleta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ColetaResponseDTO {

    private Long id;
    private LocalDate data;
    private BigDecimal volume;
    private String status;
    private String turno;
    private String observacoes;
    private DoadoraResponseDTO doadora;

    public ColetaResponseDTO(Coleta coleta) {
        id = coleta.getId();
        data = coleta.getData();
        volume = coleta.getVolume();
        status = coleta.getStatus();
        turno = coleta.getTurno();
        observacoes = coleta.getObservacoes();
        doadora = new DoadoraResponseDTO(coleta.getDoadora());
    }
}