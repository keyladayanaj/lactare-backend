package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Profissional;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProfissionalRequestDTO {

    @NotBlank(message = "Campo nome é obrigatório")
    @Size(min = 3, max = 150, message = "O campo nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @NotBlank(message = "Campo email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(example = "ana.souza@lactare.com.br")
    private String email;

    @NotBlank(message = "Campo telefone é obrigatório")
    @Size(max = 20)
    @Schema(example = "11999990001")
    private String telefone;

    @NotBlank(message = "Campo cargo é obrigatório")
    @Size(max = 50)
    @Schema(example = "Pediatra")
    private String cargo;

    public ProfissionalRequestDTO(Profissional profissional) {
        nome = profissional.getNome();
        email = profissional.getEmail();
        telefone = profissional.getTelefone();
        cargo = profissional.getCargo();
    }

}