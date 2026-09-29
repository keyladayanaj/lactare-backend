package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.InformacaoRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.InformacaoResponseDTO;
import com.github.lara.sodre.ms_lactech.service.InformacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/informacoes")
public class InformacaoController {

    @Autowired
    private InformacaoService informacaoService;

    @GetMapping
    public ResponseEntity<List<InformacaoResponseDTO>> getAllInformacoes() {

        List<InformacaoResponseDTO> informacoes = informacaoService.findAllInformacoes();

        return ResponseEntity.ok(informacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformacaoResponseDTO> getInformacaoById(@PathVariable Long id) {

        InformacaoResponseDTO informacaoDto = informacaoService.findInformacaoById(id);
        return ResponseEntity.ok(informacaoDto);
    }

    @PostMapping
    public ResponseEntity<InformacaoResponseDTO> createInformacao(@RequestBody @Valid
                                                                  InformacaoRequestDTO inputDTO) {

        InformacaoResponseDTO informacaoDto = informacaoService.saveInformacao(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(informacaoDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(informacaoDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InformacaoResponseDTO> updateInformacao(@PathVariable Long id,
                                                                  @Valid @RequestBody
                                                                  InformacaoRequestDTO inputDTO) {

        InformacaoResponseDTO informacaoDto = informacaoService.updateInformacao(id, inputDTO);
        return ResponseEntity.ok(informacaoDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInformacao(@PathVariable Long id) {

        informacaoService.deleteInformacaoById(id);

        return ResponseEntity.noContent().build();
    }

}