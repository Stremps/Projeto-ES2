package com.example.apiparticipantes.dto;

// DTO para receber o ID do tipo de participação na inscrição do evento
public class EventoInscricaoRequest {

    private Long tipoParticipacaoId;

    public Long getTipoParticipacaoId() {
        return tipoParticipacaoId;
    }

    public void setTipoParticipacaoId(Long tipoParticipacaoId) {
        this.tipoParticipacaoId = tipoParticipacaoId;
    }
}