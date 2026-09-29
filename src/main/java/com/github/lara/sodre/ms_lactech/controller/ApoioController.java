package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.ApoioRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ApoioResponseDTO;
import com.github.lara.sodre.ms_lactech.service.ApoioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/apoios")
public class ApoioController {

    @Autowired
    private ApoioService apoioService;

    @GetMapping
    public ResponseEntity<List<ApoioResponseDTO>> getAllApoios() {

        List<ApoioResponseDTO> apoios = apoioService.findAllApoios();

        return ResponseEntity.ok(apoios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApoioResponseDTO> getApoioById(@PathVariable Long id) {

        ApoioResponseDTO apoioDto = apoioService.findApoioById(id);
        return ResponseEntity.ok(apoioDto);
    }

    @PostMapping
    public ResponseEntity<ApoioResponseDTO> createApoio(@RequestBody @Valid
                                                        ApoioRequestDTO inputDTO) {

        ApoioResponseDTO apoioDto = apoioService.saveApoio(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(apoioDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(apoioDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApoioResponseDTO> updateApoio(@PathVariable Long id,
                                                        @Valid @RequestBody
                                                        ApoioRequestDTO inputDTO) {

        ApoioResponseDTO apoioDto = apoioService.updateApoio(id, inputDTO);
        return ResponseEntity.ok(apoioDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApoio(@PathVariable Long id) {

        apoioService.deleteApoioById(id);

        return ResponseEntity.noContent().build();
    }
}