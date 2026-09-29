package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "SERVICO")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_apoio", nullable = false)
    private Apoio apoio;

    @ManyToOne
    @JoinColumn(name = "id_profissional", nullable = false)
    private Profissional profissional;

    @Column(name = "tipo_suporte", nullable = false, length = 30)
    private String tipoSuporte;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "horario", nullable = false, length = 5)
    private String horario;

    @Column(name = "informacoes_adicionais", length = 500)
    private String informacoesAdicionais;
}
