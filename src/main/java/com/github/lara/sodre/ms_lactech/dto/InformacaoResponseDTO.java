package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Informacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InformacaoResponseDTO {

    private Long id;
    private String idadeBebe;
    private String amamentando;
    private String usoMedicamento;
    private String metodoColeta;
    private String alergia;
    private String doencaCronicas;
    private String observacoes;
    private String fuma;
    private String alcool;
    private String consentimento;
    private DoadoraResponseDTO doadora;

    public InformacaoResponseDTO(Informacao informacao) {
        id = informacao.getId();
        idadeBebe = informacao.getIdadeBebe();
        amamentando = informacao.getAmamentando();
        usoMedicamento = informacao.getUsoMedicamento();
        metodoColeta = informacao.getMetodoColeta();
        alergia = informacao.getAlergia();
        doencaCronicas = informacao.getDoencaCronicas();
        observacoes = informacao.getObservacoes();
        fuma = informacao.getFuma();
        alcool = informacao.getAlcool();
        consentimento = informacao.getConsentimento();
        doadora = new DoadoraResponseDTO(informacao.getDoadora());
    }
}