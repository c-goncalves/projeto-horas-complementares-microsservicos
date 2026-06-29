package br.edu.ifsp.horas.auth.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prontuaria_ra", nullable = false, unique = true)
    private String prontuarioRa;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role;

    // Getters e Setters básicos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProntuarioRa() { return prontuarioRa; }
    public void setProntuarioRa(String prontuarioRa) { this.prontuarioRa = prontuarioRa; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}