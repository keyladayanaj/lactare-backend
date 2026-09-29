package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Consulta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsultaResponseDTO {

    private Long id;
    private LocalDate data;
    private String horario;
    private String motivo;
    private String observacoes;
    private String status;
    private DoadoraResponseDTO doadora;
    private ApoioResponseDTO apoio;
    private ProfissionalResponseDTO profissional;

    public ConsultaResponseDTO(Consulta consulta) {
        id = consulta.getId();
        data = consulta.getData();
        horario = consulta.getHorario();
        motivo = consulta.getMotivo();
        observacoes = consulta.getObservacoes();
        status = consulta.getStatus();
        doadora = new DoadoraResponseDTO(consulta.getDoadora());
        apoio = consulta.getApoio() == null ? null : new ApoioResponseDTO(consulta.getApoio());
        profissional = consulta.getProfissional() == null ? null : new ProfissionalResponseDTO(consulta.getProfissional());
    }
}