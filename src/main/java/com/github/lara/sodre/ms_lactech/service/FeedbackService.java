package com.github.lara.sodre.ms_lactech.service;

import com.github.lara.sodre.ms_lactech.dto.FeedbackRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.FeedbackResponseDTO;
import com.github.lara.sodre.ms_lactech.entities.Coleta;
import com.github.lara.sodre.ms_lactech.entities.Doadora;
import com.github.lara.sodre.ms_lactech.entities.Feedback;
import com.github.lara.sodre.ms_lactech.exceptions.DatabaseException;
import com.github.lara.sodre.ms_lactech.exceptions.ResourceNotFoundException;
import com.github.lara.sodre.ms_lactech.repositories.ColetaRepository;
import com.github.lara.sodre.ms_lactech.repositories.DoadoraRepository;
import com.github.lara.sodre.ms_lactech.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private DoadoraRepository doadoraRepository;

    @Autowired
    private ColetaRepository coletaRepository;

    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> findByDoadora(Long doadoraId) {
        if (!doadoraRepository.existsById(doadoraId)) {
            throw new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId);
        }
        return feedbackRepository.findByDoadoraIdOrderByDataCriacaoDesc(doadoraId)
                .stream().map(FeedbackResponseDTO::new).toList();
    }

    @Transactional
    public FeedbackResponseDTO criar(Long doadoraId, FeedbackRequestDTO dto) {
        Doadora doadora = doadoraRepository.findById(doadoraId)
                .orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada. ID: " + doadoraId));

        Coleta coleta = coletaRepository.findById(dto.getColetaId())
                .orElseThrow(() -> new ResourceNotFoundException("Coleta não encontrada. ID: " + dto.getColetaId()));

        if (coleta.getDoadora() == null || !doadoraId.equals(coleta.getDoadora().getId())) {
            throw new DatabaseException("A coleta informada não pertence à doadora logada.");
        }

        if (!"REALIZADA".equalsIgnoreCase(coleta.getStatus())) {
            throw new DatabaseException("O feedback só pode ser enviado depois que a coleta for realizada.");
        }

        if (feedbackRepository.existsByDoadoraIdAndColetaId(doadoraId, coleta.getId())) {
            throw new DatabaseException("Esta coleta já possui um feedback.");
        }

        Feedback feedback = new Feedback();
        feedback.setDoadora(doadora);
        feedback.setColeta(coleta);
        feedback.setNota(dto.getNota());
        feedback.setComentario(dto.getComentario() == null || dto.getComentario().isBlank()
                ? null : dto.getComentario().trim());

        return new FeedbackResponseDTO(feedbackRepository.save(feedback));
    }
}
