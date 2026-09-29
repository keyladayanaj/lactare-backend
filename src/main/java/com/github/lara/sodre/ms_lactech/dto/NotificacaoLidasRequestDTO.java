package com.github.lara.sodre.ms_lactech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificacaoLidasRequestDTO {

    @NotEmpty(message = "Informe ao menos uma notificação")
    @Size(max = 200, message = "Máximo de 200 notificações por requisição")
    private List<@NotBlank(message = "Chave da notificação inválida")
                 @Size(max = 120, message = "Chave da notificação muito longa") String> chaves;
}
