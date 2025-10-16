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

    public UnidadeFederacao() {} // Construtor vazio para o JPA

    public UnidadeFederacao(String siglaUf, String nomeUf) { // Construtor com argumentos
        this.siglaUf = siglaUf;
        this.nomeUf = nomeUf;
    }

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
