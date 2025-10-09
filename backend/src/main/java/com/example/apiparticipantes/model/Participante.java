package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "participante")
public class Participante {

    @Id
    @Column(name = "id_participante", length = 255, nullable = false, updatable = false)
    private String idParticipante; // Armazena o UUID como String

    @Column(name = "nome_participante", length = 255, nullable = false)
    private String nomeParticipante;

    @Column(name = "email_participante", length = 255, nullable = false, unique = true)
    private String emailParticipante;

    @Column(name = "telefone_participante", length = 15)
    private String telefoneParticipante;

    @Column(name = "senha_participante", nullable = false)
    private String senhaParticipante;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false, length = 20)
    private Cargo cargo; // ALUNO || PROFESSOR || EXTERNO

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco_participante")
    private Endereco endereco; // Pode ser nulo (participante sem endereço ainda)

    public Participante() {
        // Gera automaticamente um UUID ao criar o participante
        this.idParticipante = UUID.randomUUID().toString();
    }

    // Getters e Setters
    public String getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(String idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public String getEmailParticipante() {
        return emailParticipante;
    }

    public void setEmailParticipante(String emailParticipante) {
        this.emailParticipante = emailParticipante;
    }

    public String getTelefoneParticipante() {
        return telefoneParticipante;
    }

    public void setTelefoneParticipante(String telefoneParticipante) {
        this.telefoneParticipante = telefoneParticipante;
    }

    public String getSenhaParticipante() {
        return senhaParticipante;
    }

    public void setSenhaParticipante(String senhaParticipante) {
        this.senhaParticipante = senhaParticipante;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
