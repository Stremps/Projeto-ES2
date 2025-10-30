package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "participante")
public class Participante {

    @Id
    @Column(name = "id_participante", length = 255, nullable = false, updatable = false)
    private String idParticipante;

    @Column(name = "nome_participante", length = 255, nullable = false)
    private String nomeParticipante;

    @Column(name = "email_participante", length = 255, nullable = false, unique = true)
    private String emailParticipante;

    @Column(name = "telefone_participante", length = 15)
    private String telefoneParticipante;

    @Column(name = "senha_participante", nullable = false)
    private String senhaParticipante;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true; // Aqui é o DELETE por meio da Lógica de exclusão (o mais usado hoje em dia nas empresas). Por padrão, o participante está ativo

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false, length = 20)
    private Cargo cargo; // ADMIN, ALUNO, PROFESSOR, EXTERNO

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco_participante")
    private Endereco endereco;

    // 🔹 Relação com inscrições (aluno/professor/externo)
    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL)
    private List<InscricaoPalestra> inscricoesPalestras;

    // O campo que você provavelmente adicionou e que causou o erro
    @OneToMany(mappedBy = "criador") // O nome "criador" aqui DEVE ser o mesmo do campo em Evento.java
    private List<Evento> eventosCriados;

    public Participante() {
        this.idParticipante = UUID.randomUUID().toString();
        this.ativo = true; // Assume que os novos part. sejam ativos
    }

    public List<Evento> getEventosCriados() {
        return eventosCriados;
    }

    public void setEventosCriados(List<Evento> eventosCriados) {
        this.eventosCriados = eventosCriados;
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
    public List<InscricaoPalestra> getInscricoesPalestras() {
        return inscricoesPalestras;
    }

    public void setInscricoesPalestras(List<InscricaoPalestra> inscricoesPalestras) {
        this.inscricoesPalestras = inscricoesPalestras;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
