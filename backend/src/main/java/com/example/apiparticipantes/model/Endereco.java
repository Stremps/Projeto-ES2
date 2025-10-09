package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long idEndereco;

    @Column(name = "numero")
    private String numero;

    @Column(name = "cep", length = 8)
    private String cep;

    @Column(name = "complemento")
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "id_logradouro")
    private Logradouro logradouro;

    @ManyToOne
    @JoinColumn(name = "id_bairro")
    private Bairro bairro;

    public Endereco() {}
    // getters/setters


    public Long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Long idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }
}
