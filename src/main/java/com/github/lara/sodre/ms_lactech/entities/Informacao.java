package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "INFORMACAO")
public class Informacao {

    @Id
    @Column(name = "id_informacao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idade_bebe", nullable = false, length = 20)
    private String idadeBebe;

    @Column(name = "amamentando", nullable = false, length = 5)
    private String amamentando;

    @Column(name = "uso_medicamento", nullable = false, length = 5)
    private String usoMedicamento;

    @Column(name = "metodo_coleta", nullable = false, length = 5)
    private String metodoColeta;

    @Column(name = "alergia", length = 255)
    private String alergia;

    @Column(name = "doenca_cronicas", length = 255)
    private String doencaCronicas;

    @Column(name = "observacoes", length = 255)
    private String observacoes;

    @Column(name = "fuma", nullable = false, length = 5)
    private String fuma;

    @Column(name = "alcool", nullable = false, length = 5)
    private String alcool;

    @Column(name = "consentimento", nullable = false, length = 5)
    private String consentimento;

    @OneToOne
    @JoinColumn(name = "id_doadora", nullable = false, unique = true)
    private Doadora doadora;
}