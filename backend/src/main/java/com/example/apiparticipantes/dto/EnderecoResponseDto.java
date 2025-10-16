package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.Endereco;

public class EnderecoResponseDto {
    private Long id;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public EnderecoResponseDto(Endereco endereco) {
        this.id = endereco.getIdEndereco();
        this.logradouro = endereco.getLogradouro().getNomeLogradouro();
        this.numero = endereco.getNumero();
        this.bairro = endereco.getBairro().getNomeBairro();
        this.cidade = endereco.getCidade().getNomeCidade();
        this.uf = endereco.getCidade().getUnidadeFederacao().getSiglaUf();
        this.cep = endereco.getCep();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}