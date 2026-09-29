package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.NotificacaoLidasRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.NotificacaoLidasResponseDTO;
import com.github.lara.sodre.ms_lactech.service.NotificacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/doadora/{doadoraId}/lidas")
    public ResponseEntity<NotificacaoLidasResponseDTO> lidas(@PathVariable Long doadoraId) {
        return ResponseEntity.ok(notificacaoService.lidas(doadoraId));
    }

    @PutMapping("/doadora/{doadoraId}/lidas")
    public ResponseEntity<NotificacaoLidasResponseDTO> marcarLidas(
            @PathVariable Long doadoraId,
            @Valid @RequestBody NotificacaoLidasRequestDTO dto) {
        return ResponseEntity.ok(notificacaoService.marcarLidas(doadoraId, dto.getChaves()));
    }
}
