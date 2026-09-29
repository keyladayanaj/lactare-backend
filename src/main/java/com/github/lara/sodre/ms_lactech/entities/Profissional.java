package com.github.lara.sodre.ms_lactech.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "PROFISSIONAL")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profissional")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cargo", nullable = false, length = 30)
    private String cargo;

    @Column(name = "usuario", length = 80, unique = true)
    private String usuario;

    @Column(name = "senha", length = 120)
    private String senha;

    @OneToMany(mappedBy = "profissional")
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "profissional")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @OneToMany(mappedBy = "profissional")
    private List<Servico> servicos = new ArrayList<>();
}