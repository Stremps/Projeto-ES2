package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_logradouro")
public class TipoLogradouro {
    @Id
    @Column(name = "id_tipo_logradouro", length = 100)
    private String idTipoLogradouro;

    @Column(name = "nome_tipo_logradouro")
    private String nomeTipoLogradouro;

    public TipoLogradouro() {}

    public TipoLogradouro(String idTipoLogradouro, String nomeTipoLogradouro) {
        this.idTipoLogradouro = idTipoLogradouro;
        this.nomeTipoLogradouro = nomeTipoLogradouro;
    }

    public String getIdTipoLogradouro() {
        return idTipoLogradouro;
    }

    public void setIdTipoLogradouro(String idTipoLogradouro) {
        this.idTipoLogradouro = idTipoLogradouro;
    }

    public String getNomeTipoLogradouro() {
        return nomeTipoLogradouro;
    }

    public void setNomeTipoLogradouro(String nomeTipoLogradouro) {
        this.nomeTipoLogradouro = nomeTipoLogradouro;
    }
}
