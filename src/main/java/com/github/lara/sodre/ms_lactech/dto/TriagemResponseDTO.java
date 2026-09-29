package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Triagem;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter
public class TriagemResponseDTO {
    private Long id;
    private Long doadoraId;
    private String doadoraNome;
    private String doadoraEmail;
    private LocalDateTime dataEnvio;
    private String status;
    private String amamentando;
    private String idadeBebe;
    private String medicamento;
    private String fuma;
    private String examesRecentes;
    private String jaDoou;
    private LocalDateTime analisadaEm;

    public TriagemResponseDTO(Triagem t) {
        id=t.getId(); doadoraId=t.getDoadora().getId(); doadoraNome=t.getDoadora().getNome();
        doadoraEmail=t.getDoadora().getEmail(); dataEnvio=t.getDataEnvio(); status=t.getStatus();
        amamentando=t.getAmamentando(); idadeBebe=t.getIdadeBebe(); medicamento=t.getMedicamento();
        fuma=t.getFuma(); examesRecentes=t.getExamesRecentes(); jaDoou=t.getJaDoou(); analisadaEm=t.getAnalisadaEm();
    }
}
