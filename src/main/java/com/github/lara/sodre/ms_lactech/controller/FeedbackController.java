package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.FeedbackRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.FeedbackResponseDTO;
import com.github.lara.sodre.ms_lactech.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/doadora/{doadoraId}")
    public ResponseEntity<List<FeedbackResponseDTO>> listar(@PathVariable Long doadoraId) {
        return ResponseEntity.ok(feedbackService.findByDoadora(doadoraId));
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> criar(
            @RequestAttribute("lactareDoadoraId") Long doadoraId,
            @Valid @RequestBody FeedbackRequestDTO dto) {
        return ResponseEntity.status(201).body(feedbackService.criar(doadoraId, dto));
    }
}
