package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Profissional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProfissionalResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cargo;

    public ProfissionalResponseDTO(Profissional profissional) {
        id = profissional.getId();
        nome = profissional.getNome();
        email = profissional.getEmail();
        telefone = profissional.getTelefone();
        cargo = profissional.getCargo();
    }
}