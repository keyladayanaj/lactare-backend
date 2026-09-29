package com.github.lara.sodre.ms_lactech.dto;

import com.github.lara.sodre.ms_lactech.entities.Informacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InformacaoRequestDTO {

    @NotBlank(message = "Campo idade do bebê é obrigatório")
    @Schema(example = "5 meses")
    @Size(max = 20)
    private String idadeBebe;

    @NotBlank(message = "Informe se está amamentando (SIM/NAO)")
    @Schema(example = "SIM")
    @Size(max = 5)
    private String amamentando;

    @NotBlank(message = "Informe uso de medicamento (SIM/NAO)")
    @Schema(example = "NAO")
    @Size(max = 5)
    private String usoMedicamento;

    @NotBlank(message = "Campo método de coleta é obrigatório")
    @Schema(example = "MA")
    @Size(max = 5)
    private String metodoColeta;

    @NotBlank(message = "Informe se possui alguma alergia")
    @Schema(example = "Nenhuma")
    @Size(max = 255)
    private String alergia;


    @NotBlank(message = "Informe se possui doenças crônicas")
    @Schema(example = "Nenhuma")
    @Size(max = 255)
    private String doencaCronicas;

    @NotBlank(message = "Campo observações de coleta é obrigatório")
    @Schema(example = "Nenhuma observação")
    @Size(max = 255)
    private String observacoes;

    @NotBlank(message = "Informe se fuma (SIM/NAO)")
    @Schema(example = "NAO")
    @Size(max = 5)
    private String fuma;

    @NotBlank(message = "Informe se consome álcool (SIM/NAO)")
    @Schema(example = "NAO")
    @Size(max = 5)
    private String alcool;

    @NotBlank(message = "Consentimento é obrigatório (SIM/NAO)")
    @Schema(example = "SIM")
    @Size(max = 5)
    private String consentimento;

    @NotNull(message = "Campo doadoraId é obrigatório")
    @Schema(example = "1")
    private Long doadoraId;

    public InformacaoRequestDTO(Informacao informacao) {
        idadeBebe = informacao.getIdadeBebe();
        amamentando = informacao.getAmamentando();
        usoMedicamento = informacao.getUsoMedicamento();
        metodoColeta = informacao.getMetodoColeta();
        alergia = informacao.getAlergia();
        doencaCronicas = informacao.getDoencaCronicas();
        observacoes = informacao.getObservacoes();
        fuma = informacao.getFuma();
        alcool = informacao.getAlcool();
        consentimento = informacao.getConsentimento();
        doadoraId = informacao.getDoadora().getId();
    }
}