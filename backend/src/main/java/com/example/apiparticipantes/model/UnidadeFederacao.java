package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "unidade_federacao")
public class UnidadeFederacao {
    @Id
    @Column(name = "sigla_uf", length = 100)
    private String siglaUf;

    @Column(name = "nome_uf")
    private String nomeUf;

    public UnidadeFederacao() {}
    // getters/setters


    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }

    public String getNomeUf() {
        return nomeUf;
    }

    public void setNomeUf(String nomeUf) {
        this.nomeUf = nomeUf;
    }
}
