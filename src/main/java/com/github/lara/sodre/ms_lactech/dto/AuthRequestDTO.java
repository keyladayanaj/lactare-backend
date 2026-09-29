package com.github.lara.sodre.ms_lactech.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@AllArgsConstructor @NoArgsConstructor @Getter
public class AuthRequestDTO { @NotBlank private String usuario; @NotBlank private String senha; }
