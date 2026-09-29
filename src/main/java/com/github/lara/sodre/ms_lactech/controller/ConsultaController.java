package com.github.lara.sodre.ms_lactech.controller;

import com.github.lara.sodre.ms_lactech.dto.ConsultaRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ExameStatusRequestDTO;
import com.github.lara.sodre.ms_lactech.dto.ConsultaResponseDTO;
import com.github.lara.sodre.ms_lactech.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> getAllConsultas() {

        List<ConsultaResponseDTO> consultas = consultaService.findAllConsultas();

        return ResponseEntity.ok(consultas);
    }

    @PostMapping("/exame-medico")
    public ResponseEntity<ConsultaResponseDTO> agendarExame(@Valid @RequestBody com.github.lara.sodre.ms_lactech.dto.ExameMedicoRequestDTO dto) { return ResponseEntity.status(201).body(consultaService.agendarExame(dto)); }

    @GetMapping("/doadora/{doadoraId}/exame-medico")
    public ResponseEntity<ConsultaResponseDTO> exameDaDoadora(@PathVariable Long doadoraId) { return ResponseEntity.ok(consultaService.exameDaDoadora(doadoraId)); }

    @PutMapping("/{id}/aprovar-exame")
    public ResponseEntity<ConsultaResponseDTO> aprovarExame(@PathVariable Long id) { return ResponseEntity.ok(consultaService.aprovarExame(id)); }

    @PutMapping("/{id}/confirmar-exame")
    public ResponseEntity<ConsultaResponseDTO> confirmarAgendamentoExame(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.confirmarAgendamentoExame(id));
    }

    @PutMapping("/{id}/realizar-exame")
    public ResponseEntity<ConsultaResponseDTO> realizarExame(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.realizarExame(id));
    }

    @PutMapping("/{id}/resultado-exame")
    public ResponseEntity<ConsultaResponseDTO> resultadoExame(@PathVariable Long id, @Valid @RequestBody ExameStatusRequestDTO dto) {
        return ResponseEntity.ok(consultaService.registrarResultadoExame(id, dto.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> getConsultaById(@PathVariable Long id) {

        ConsultaResponseDTO consultaDto = consultaService.findConsultaById(id);
        return ResponseEntity.ok(consultaDto);
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> createConsulta(@RequestBody @Valid
                                                              ConsultaRequestDTO inputDTO) {

        ConsultaResponseDTO consultaDto = consultaService.saveConsulta(inputDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(consultaDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(consultaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> updateConsulta(@PathVariable Long id,
                                                              @Valid @RequestBody
                                                              ConsultaRequestDTO inputDTO) {

        ConsultaResponseDTO consultaDto = consultaService.updateConsulta(id, inputDTO);
        return ResponseEntity.ok(consultaDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {

        consultaService.deleteConsultaById(id);

        return ResponseEntity.noContent().build();
    }
}