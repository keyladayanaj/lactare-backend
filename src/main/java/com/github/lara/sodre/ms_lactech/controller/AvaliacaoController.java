package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.AvaliacaoRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.AvaliacaoResponseDTO;
import com.github.lara.sodre.ms_lactech.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponseDTO>> getAllAvaliacoes() {

        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.findAllAvaliacoes();

        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> getAvaliacaoById(@PathVariable Long id) {

        AvaliacaoResponseDTO avaliacaoDto = avaliacaoService.findAvaliacaoById(id);
        return ResponseEntity.ok(avaliacaoDto);
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> createAvaliacao(@RequestBody @Valid
                                                                AvaliacaoRequestDTO inputDTO) {

        AvaliacaoResponseDTO avaliacaoDto = avaliacaoService.saveAvaliacao(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(avaliacaoDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(avaliacaoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> updateAvaliacao(@PathVariable Long id,
                                                                @Valid @RequestBody
                                                                AvaliacaoRequestDTO inputDTO) {

        AvaliacaoResponseDTO avaliacaoDto = avaliacaoService.updateAvaliacao(id, inputDTO);
        return ResponseEntity.ok(avaliacaoDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable Long id) {

        avaliacaoService.deleteAvaliacaoById(id);

        return ResponseEntity.noContent().build();
    }


}