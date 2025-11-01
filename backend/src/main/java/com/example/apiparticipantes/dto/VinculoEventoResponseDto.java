package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.VinculoEvento;
import java.time.LocalDate;

public class VinculoEventoResponseDto {
    private Long id;
    private String participanteNome;
    private String eventoNome;
    private String tipoParticipacaoNome;
    private String status; // <-- Adicionamos o Status
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public VinculoEventoResponseDto(VinculoEvento vinculo) {
        this.id = vinculo.getId();
        this.participanteNome = vinculo.getParticipante().getNomeParticipante();
        this.eventoNome = vinculo.getEvento().getNome();
        this.tipoParticipacaoNome = vinculo.getTipoParticipacao().getNome();
        this.status = vinculo.getStatus().toString(); // <-- Adicionamos o Status
        this.dataInicio = vinculo.getDataInicio();
        this.dataFim = vinculo.getDataFim();
    }

    // Getters (só getters são necessários para resposta)
    public Long getId() { return id; }
    public String getParticipanteNome() { return participanteNome; }
    public String getEventoNome() { return eventoNome; }
    public String getTipoParticipacaoNome() { return tipoParticipacaoNome; }
    public String getStatus() { return status; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
}