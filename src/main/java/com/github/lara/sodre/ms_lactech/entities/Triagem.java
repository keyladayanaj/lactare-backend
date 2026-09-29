package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "TRIAGEM")
public class Triagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_triagem")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doadora", nullable = false)
    private Doadora doadora;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "amamentando", length = 20) private String amamentando;
    @Column(name = "idade_bebe", length = 50) private String idadeBebe;
    @Column(name = "medicamento", length = 20) private String medicamento;
    @Column(name = "fuma", length = 20) private String fuma;
    @Column(name = "exames_recentes", length = 20) private String examesRecentes;
    @Column(name = "ja_doou", length = 20) private String jaDoou;

    @Column(name = "analisada_em")
    private LocalDateTime analisadaEm;
}
