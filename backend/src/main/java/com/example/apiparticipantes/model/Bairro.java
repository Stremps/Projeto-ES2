package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bairro")
public class Bairro {
    @Id
    @Column(name = "id_bairro", length = 100)
    private String idBairro;

    @Column(name = "nome_bairro")
    private String nomeBairro;

    public Bairro() {}

    public Bairro(String idBairro, String nomeBairro) {
        this.idBairro = idBairro;
        this.nomeBairro = nomeBairro;
    }

    public String getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(String idBairro) {
        this.idBairro = idBairro;
    }

    public String getNomeBairro(){
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }
}
