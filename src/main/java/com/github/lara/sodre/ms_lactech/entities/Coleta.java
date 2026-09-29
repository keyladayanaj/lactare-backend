package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "COLETA")
public class Coleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleta")
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "volume", precision = 6, scale = 2)
    private BigDecimal volume;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "turno", nullable = false, length = 20)
    private String turno;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "id_doadora", nullable = false)
    private Doadora doadora;
}