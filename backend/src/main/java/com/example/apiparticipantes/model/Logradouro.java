package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "logradouro")
public class Logradouro {
    @Id
    @Column(name = "id_logradouro", length = 100)
    private String idLogradouro;

    @Column(name = "nome_logradouro")
    private String nomeLogradouro;

    @ManyToOne
    @JoinColumn(name = "id_tipo_logradouro")
    private TipoLogradouro tipoLogradouro;

    public Logradouro() {}
    // getters/setters


    public String getIdLogradouro() {
        return idLogradouro;
    }

    public void setIdLogradouro(String idLogradouro) {
        this.idLogradouro = idLogradouro;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public TipoLogradouro getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }
}
