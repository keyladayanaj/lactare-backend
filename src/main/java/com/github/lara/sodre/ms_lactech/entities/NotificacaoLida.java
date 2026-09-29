package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registro de que a doadora já visualizou uma notificação.
 * As notificações em si são eventos reais da jornada (triagem, exame, kit, coleta);
 * aqui fica apenas o estado "lida", por doadora, identificado pela chave do evento.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "NOTIFICACAO_LIDA",
       uniqueConstraints = @UniqueConstraint(name = "uk_notificacao_lida_doadora_chave",
                                             columnNames = {"id_doadora", "chave"}))
public class NotificacaoLida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao_lida")
    private Long id;

    @Column(name = "chave", nullable = false, length = 120)
    private String chave;

    @Column(name = "data_leitura", nullable = false)
    private LocalDateTime dataLeitura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_doadora", nullable = false)
    private Doadora doadora;

    @PrePersist
    public void prePersist() {
        if (dataLeitura == null) {
            dataLeitura = LocalDateTime.now();
        }
    }
}
