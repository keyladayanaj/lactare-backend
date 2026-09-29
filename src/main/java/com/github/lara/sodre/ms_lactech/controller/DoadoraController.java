package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.DoadoraRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.DoadoraResponseDTO;
import com.github.lara.sodre.ms_lactech.service.DoadoraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doadoras")
public class DoadoraController {

    @Autowired
    private DoadoraService doadoraService;

    @GetMapping
    public ResponseEntity<List<DoadoraResponseDTO>> getAllDoadoras() {

        List<DoadoraResponseDTO> doadoras = doadoraService.findAllDoadora();

        return ResponseEntity.ok(doadoras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoadoraResponseDTO> getDoadoraById(@PathVariable Long id) {

        DoadoraResponseDTO doadoraDto = doadoraService.findDoadoraById(id);
        return ResponseEntity.ok(doadoraDto);
    }

    @PostMapping
    public ResponseEntity<DoadoraResponseDTO> createDoadora(@RequestBody @Valid
                                                            DoadoraRequestDTO inputDTO) {

        DoadoraResponseDTO doadoraDto = doadoraService.saveDoadora(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(doadoraDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(doadoraDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoadoraResponseDTO> updateDoadora(@PathVariable Long id,
                                                            @Valid @RequestBody
                                                            DoadoraRequestDTO inputDTO) {

        DoadoraResponseDTO doadoraDto = doadoraService.updateDoadora(id, inputDTO);
        return ResponseEntity.ok(doadoraDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoadora(@PathVariable Long id) {

        doadoraService.deleteDoadoraById(id);

        return ResponseEntity.noContent().build();
    }

}