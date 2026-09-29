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
@Table(name = "APOIO")
public class Apoio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_apoio")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "usuario", nullable = false, length = 50, unique = true)
    private String usuario;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "endereco", nullable = false, length = 150)
    private String endereco;

    @Column(name = "rg", nullable = false, length = 15, unique = true)
    private String rg;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cidade", nullable = false, length = 60)
    private String cidade;

    @Column(name = "bairro", nullable = false, length = 60)
    private String bairro;

    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @OneToMany(mappedBy = "apoio")
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "apoio")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @OneToMany(mappedBy = "apoio")
    private List<Servico> servicos = new ArrayList<>();
}