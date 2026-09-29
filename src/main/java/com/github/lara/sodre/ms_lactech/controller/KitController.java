package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.KitResponseDTO;
import com.github.lara.sodre.ms_lactech.service.KitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kits")
public class KitController {
    @Autowired private KitService kitService;

    @GetMapping
    public ResponseEntity<List<KitResponseDTO>> getAll() { return ResponseEntity.ok(kitService.findAll()); }

    @GetMapping("/doadora/{doadoraId}")
    public ResponseEntity<KitResponseDTO> getByDoadora(@PathVariable Long doadoraId) { return ResponseEntity.ok(kitService.findByDoadora(doadoraId)); }

    @PutMapping("/{id}/preparar")
    public ResponseEntity<KitResponseDTO> preparar(@PathVariable Long id) { return ResponseEntity.ok(kitService.preparar(id)); }

    @PutMapping("/{id}/enviar")
    public ResponseEntity<KitResponseDTO> enviar(@PathVariable Long id) { return ResponseEntity.ok(kitService.enviar(id)); }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<KitResponseDTO> entregar(@PathVariable Long id) { return ResponseEntity.ok(kitService.entregar(id)); }
}
