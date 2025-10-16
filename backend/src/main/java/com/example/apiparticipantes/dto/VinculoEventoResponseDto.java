package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.VinculoEvento;

public class VinculoEventoResponseDto {
    private Long id;
    private String nomeParticipante;
    private String nomeEvento;
    private String tipoParticipacao;

    public VinculoEventoResponseDto(VinculoEvento vinculo) {
        this.id = vinculo.getId();
        this.nomeParticipante = vinculo.getParticipante().getNomeParticipante();
        this.nomeEvento = vinculo.getEvento().getNome();
        this.tipoParticipacao = vinculo.getTipoParticipacao().getNome();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getTipoParticipacao() {
        return tipoParticipacao;
    }

    public void setTipoParticipacao(String tipoParticipacao) {
        this.tipoParticipacao = tipoParticipacao;
    }
}