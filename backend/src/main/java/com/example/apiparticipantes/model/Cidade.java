package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cidade")
public class Cidade {
    @Id
    @Column(name = "id_cidade", length = 100)
    private String idCidade;

    @Column(name = "nome_cidade")
    private String nomeCidade;

    @ManyToOne
    @JoinColumn(name = "sigla_uf")
    private UnidadeFederacao unidadeFederacao;

    public Cidade() {}
    // getters/setters


    public String getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(String idCidade) {
        this.idCidade = idCidade;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public UnidadeFederacao getUnidadeFederacao() {
        return unidadeFederacao;
    }

    public void setUnidadeFederacao(UnidadeFederacao unidadeFederacao) {
        this.unidadeFederacao = unidadeFederacao;
    }
}
