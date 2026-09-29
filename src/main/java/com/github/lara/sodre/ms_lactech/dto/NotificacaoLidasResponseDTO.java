package com.github.lara.sodre.ms_lactech.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificacaoLidasResponseDTO {

    private final List<String> chaves;

    public NotificacaoLidasResponseDTO(List<String> chaves) {
        this.chaves = chaves;
    }
}
