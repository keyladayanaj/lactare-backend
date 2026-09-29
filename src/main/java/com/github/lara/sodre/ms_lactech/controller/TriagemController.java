package com.github.lara.sodre.ms_lactech.controller;
import com.github.lara.sodre.ms_lactech.dto.*;
import com.github.lara.sodre.ms_lactech.service.TriagemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/triagens")
public class TriagemController {
 private final TriagemService service; public TriagemController(TriagemService s){service=s;}
 @GetMapping public ResponseEntity<List<TriagemResponseDTO>> listar(){return ResponseEntity.ok(service.listar());}
 @GetMapping("/{id}") public ResponseEntity<TriagemResponseDTO> porId(@PathVariable Long id){return ResponseEntity.ok(service.porId(id));}
 @GetMapping("/doadora/{doadoraId}") public ResponseEntity<TriagemResponseDTO> ultima(@PathVariable Long doadoraId){return ResponseEntity.ok(service.ultimaDaDoadora(doadoraId));}
 @PostMapping public ResponseEntity<TriagemResponseDTO> criar(@Valid @RequestBody TriagemRequestDTO dto){return ResponseEntity.status(201).body(service.criar(dto));}
 @PatchMapping("/{id}/status") public ResponseEntity<TriagemResponseDTO> status(@PathVariable Long id,@Valid @RequestBody TriagemStatusRequestDTO dto){return ResponseEntity.ok(service.alterarStatus(id,dto.getStatus()));}
}
