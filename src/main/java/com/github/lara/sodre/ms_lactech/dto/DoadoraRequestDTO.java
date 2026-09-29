package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Doadora;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DoadoraRequestDTO {

    @NotBlank(message = "Campo cpf é obrigatório")
    @Size(max = 11, message = "O campo cpf deve ter no máximo 11 caracteres")
    @Schema(example = "12345678900")
    private String cpf;

    @NotBlank(message = "Campo rg é obrigatório")
    @Size(max = 15, message = "O campo rg deve ter no máximo 15 caracteres")
    @Schema(example = "123456789")
    private String rg;

    @NotBlank(message = "Campo nome é obrigatório")
    @Size(min = 3, max = 100, message = "O campo nome deve ter entre 3 e 100 caracteres")
    @Schema(example = "Maria Aparecida Silva")
    private String nome;

    @NotNull(message = "Campo data de nascimento é obrigatório")
    @Past(message = "A data de nascimento deve estar no passado")
    @Schema(example = "1992-03-12")
    private LocalDate dataNascimento;

    @NotBlank(message = "Campo telefone é obrigatório")
    @Size(max = 20, message = "O campo telefone deve ter no máximo 20 caracteres")
    @Schema(example = "11998765432")
    private String telefone;

    @NotBlank(message = "Campo email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(example = "maria.silva@email.com")
    private String email;

    @NotBlank(message = "Campo endereco é obrigatório")
    @Schema(example = "Rua das Flores, 142")
    private String endereco;

    @NotBlank(message = "Campo bairro é obrigatório")
    @Schema(example = "Centro")
    private String bairro;

    @NotBlank(message = "Campo cidade é obrigatório")
    @Schema(example = "São Paulo")
    private String cidade;

    @NotBlank(message = "Campo cep é obrigatório")
    @Size(max = 9, message = "O campo cep deve ter no máximo 9 caracteres")
    @Schema(example = "05000000")
    private String cep;

    @NotBlank(message = "Campo usuario é obrigatório")
    @Size(max = 20, message = "O campo usuario deve ter no máximo 20 caracteres")
    @Schema(example = "nutriz_maria")
    private String usuario;

    @NotBlank(message = "Campo senha é obrigatório")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(example = "senha456")
    private String senha;

    public DoadoraRequestDTO(Doadora doadora) {
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
        senha = doadora.getSenha();
    }
}