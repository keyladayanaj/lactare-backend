package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Apoio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApoioResponseDTO {

    private Long id;
    private String nome;
    private String usuario;
    private LocalDate dataNascimento;
    private String email;
    private String endereco;
    private String rg;
    private String cpf;
    private String telefone;
    private String cidade;
    private String bairro;
    private String cep;
    // senha nunca é exposta na resposta

    public ApoioResponseDTO(Apoio apoio) {
        id = apoio.getId();
        nome = apoio.getNome();
        usuario = apoio.getUsuario();
        dataNascimento = apoio.getDataNascimento();
        email = apoio.getEmail();
        endereco = apoio.getEndereco();
        rg = apoio.getRg();
        cpf = apoio.getCpf();
        telefone = apoio.getTelefone();
        cidade = apoio.getCidade();
        bairro = apoio.getBairro();
        cep = apoio.getCep();
    }
}