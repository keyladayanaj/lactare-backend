package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "CONSULTA")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "horario", nullable = false, length = 5)
    private String horario;

    @Column(name = "motivo", nullable = false, length = 200)
    private String motivo;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_doadora", nullable = false)
    private Doadora doadora;

    @ManyToOne
    @JoinColumn(name = "id_apoio")
    private Apoio apoio;

    @ManyToOne
    @JoinColumn(name = "id_profissional", nullable = true)
    private Profissional profissional;

    @OneToMany(mappedBy = "consulta")
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}