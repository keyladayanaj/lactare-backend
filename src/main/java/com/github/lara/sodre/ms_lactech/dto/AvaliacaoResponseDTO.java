package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Avaliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AvaliacaoResponseDTO {

    private Long id;
    private LocalDate data;
    private BigDecimal nota;
    private String comentario;
    private DoadoraResponseDTO doadora;
    private ApoioResponseDTO apoio;
    private ProfissionalResponseDTO profissional;
    private ConsultaResponseDTO consulta;

    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        id = avaliacao.getId();
        data = avaliacao.getData();
        nota = avaliacao.getNota();
        comentario = avaliacao.getComentario();
        doadora = avaliacao.getDoadora() == null ? null : new DoadoraResponseDTO(avaliacao.getDoadora());
        apoio = avaliacao.getApoio() == null ? null : new ApoioResponseDTO(avaliacao.getApoio());
        profissional = new ProfissionalResponseDTO(avaliacao.getProfissional());
        consulta = new ConsultaResponseDTO(avaliacao.getConsulta());
    }
}