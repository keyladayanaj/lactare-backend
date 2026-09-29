package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.ProfissionalRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ProfissionalResponseDTO;
import com.github.lara.sodre.ms_lactech.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> getAllProfissionais() {

        List<ProfissionalResponseDTO> profissionais = profissionalService.findAllProfissionais();

        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> getProfissionalById(@PathVariable Long id) {

        ProfissionalResponseDTO profissionalDto = profissionalService.findProfissionalById(id);
        return ResponseEntity.ok(profissionalDto);
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> createProfissional(@RequestBody @Valid
                                                                      ProfissionalRequestDTO inputDTO) {

        ProfissionalResponseDTO profissionalDto = profissionalService.saveProfissional(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(profissionalDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(profissionalDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> updateProfissional(@PathVariable Long id,
                                                                      @Valid @RequestBody
                                                                      ProfissionalRequestDTO inputDTO) {

        ProfissionalResponseDTO profissionalDto = profissionalService.updateProfissional(id, inputDTO);
        return ResponseEntity.ok(profissionalDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfissional(@PathVariable Long id) {

        profissionalService.deleteProfissionalById(id);

        return ResponseEntity.noContent().build();
    }
}