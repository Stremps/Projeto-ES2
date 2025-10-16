package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.InscricaoPalestra;
import java.time.LocalDate;

public class InscricaoPalestraResponseDto {
    private Long id;
    private LocalDate dataInscricao;
    private String nomeParticipante;
    private String tituloPalestra;

    public InscricaoPalestraResponseDto(InscricaoPalestra inscricao) {
        this.id = inscricao.getId();
        this.dataInscricao = inscricao.getData();
        this.nomeParticipante = inscricao.getParticipante().getNomeParticipante();
        this.tituloPalestra = inscricao.getPalestra().getTitulo();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDate dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public String getTituloPalestra() {
        return tituloPalestra;
    }

    public void setTituloPalestra(String tituloPalestra) {
        this.tituloPalestra = tituloPalestra;
    }
}