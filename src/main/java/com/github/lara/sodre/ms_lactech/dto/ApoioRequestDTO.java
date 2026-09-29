package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Apoio;
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
public class ApoioRequestDTO {

    @NotBlank(message = "Campo nome é obrigatório")
    @Size(min = 3, max = 100, message = "O campo nome deve ter entre 3 e 100 caracteres")
    @Schema(example = "Fátima Oliveira")
    private String nome;

    @NotBlank(message = "Campo usuario é obrigatório")
    @Size(max = 20)
    @Schema(example = "fatima_oliveira")
    private String usuario;

    @NotBlank(message = "Campo senha é obrigatório")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(example = "senha123")
    private String senha;

    @NotNull(message = "Campo data de nascimento é obrigatório")
    @Past(message = "A data de nascimento deve estar no passado")
    @Schema(example = "1990-05-10")
    private LocalDate dataNascimento;

    @NotBlank(message = "Campo email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(example = "familia.oliveira@email.com")
    private String email;

    @NotBlank(message = "Campo endereco é obrigatório")
    @Schema(example = "Rua das Palmeiras, 55")
    private String endereco;

    @NotBlank(message = "Campo rg é obrigatório")
    @Size(max = 14)
    @Schema(example = "123456789")
    private String rg;

    @NotBlank(message = "Campo cpf é obrigatório")
    @Size(max = 20)
    @Schema(example = "98765432100")
    private String cpf;

    @NotBlank(message = "Campo telefone é obrigatório")
    @Size(max = 20)
    @Schema(example = "11988887777")
    private String telefone;

    @NotBlank(message = "Campo cidade é obrigatório")
    @Size(max = 100)
    @Schema(example = "São Paulo")
    private String cidade;

    @NotBlank(message = "Campo bairro é obrigatório")
    @Schema(example = "Vila Nova")
    private String bairro;

    @NotBlank(message = "Campo cep é obrigatório")
    @Size(max = 9)
    @Schema(example = "05100000")
    private String cep;

    public ApoioRequestDTO(Apoio apoio) {
        nome = apoio.getNome();
        usuario = apoio.getUsuario();
        senha = apoio.getSenha();
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