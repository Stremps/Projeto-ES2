package com.example.apiparticipantes.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventoRequest {

    // Dados do Evento
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String descricao;

    // Dados do Endereço do Evento
    private String cep;
    private String complemento;
    private String numero;
    private String nomeBairro;
    private String nomeCidade;
    private String siglaUf;
    private String nomeLogradouro;
    private String nomeTipoLogradouro;

    // Getters e Setters para todos os campos
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getNomeBairro() { return nomeBairro; }
    public void setNomeBairro(String nomeBairro) { this.nomeBairro = nomeBairro; }
    public String getNomeCidade() { return nomeCidade; }
    public void setNomeCidade(String nomeCidade) { this.nomeCidade = nomeCidade; }
    public String getSiglaUf() { return siglaUf; }
    public void setSiglaUf(String siglaUf) { this.siglaUf = siglaUf; }
    public String getNomeLogradouro() { return nomeLogradouro; }
    public void setNomeLogradouro(String nomeLogradouro) { this.nomeLogradouro = nomeLogradouro; }
    public String getNomeTipoLogradouro() { return nomeTipoLogradouro; }
    public void setNomeTipoLogradouro(String nomeTipoLogradouro) { this.nomeTipoLogradouro = nomeTipoLogradouro; }
}