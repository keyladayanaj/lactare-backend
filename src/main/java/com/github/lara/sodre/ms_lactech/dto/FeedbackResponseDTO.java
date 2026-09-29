package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Feedback;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedbackResponseDTO {

    private final Long id;
    private final Long coletaId;
    private final String dataColeta;
    private final String turno;
    private final String statusColeta;
    private final Integer nota;
    private final String comentario;
    private final LocalDateTime dataCriacao;

    public FeedbackResponseDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.coletaId = feedback.getColeta().getId();
        this.dataColeta = feedback.getColeta().getData().toString();
        this.turno = feedback.getColeta().getTurno();
        this.statusColeta = feedback.getColeta().getStatus();
        this.nota = feedback.getNota();
        this.comentario = feedback.getComentario();
        this.dataCriacao = feedback.getDataCriacao();
    }
}
