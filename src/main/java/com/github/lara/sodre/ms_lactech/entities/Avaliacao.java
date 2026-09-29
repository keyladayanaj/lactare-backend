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
@Table(name = "AVALIACAO")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "nota", nullable = false, precision = 2, scale = 1)
    private java.math.BigDecimal nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_doadora")
    private Doadora doadora;

    @ManyToOne
    @JoinColumn(name = "id_apoio")
    private Apoio apoio;

    @ManyToOne
    @JoinColumn(name = "id_profissional", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "id_consulta")
    private Consulta consulta;
}