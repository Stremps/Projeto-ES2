package com.example.apiparticipantes.dto;

public class VinculoRequest {

    // Corresponde ao id do Evento (que é Long no seu Evento.java)
    private Long idEvento;

    // Corresponde ao id do TipoParticipacao (que é Long no seu TipoParticipacao.java)
    private Long idTipoParticipacao;

    // Getters e Setters
    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getIdTipoParticipacao() {
        return idTipoParticipacao;
    }

    public void setIdTipoParticipacao(Long idTipoParticipacao) {
        this.idTipoParticipacao = idTipoParticipacao;
    }
}