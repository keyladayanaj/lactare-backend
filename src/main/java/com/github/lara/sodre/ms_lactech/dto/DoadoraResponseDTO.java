package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Doadora;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DoadoraResponseDTO {

    private Long id;
    private String cpf;
    private String rg;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String cep;
    private String usuario;
    // senha nunca é exposta na resposta

    public DoadoraResponseDTO(Doadora doadora) {
        id = doadora.getId();
        cpf = doadora.getCpf();
        rg = doadora.getRg();
        nome = doadora.getNome();
        dataNascimento = doadora.getDataNascimento();
        telefone = doadora.getTelefone();
        email = doadora.getEmail();
        endereco = doadora.getEndereco();
        bairro = doadora.getBairro();
        cidade = doadora.getCidade();
        cep = doadora.getCep();
        usuario = doadora.getUsuario();
    }
}