package com.github.lara.sodre.ms_lactech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExameStatusRequestDTO {
    @NotBlank
    @Size(max = 20)
    private String status;
}
