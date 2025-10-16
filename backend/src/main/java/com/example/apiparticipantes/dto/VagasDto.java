package com.example.apiparticipantes.dto;

/**
 * DTO (Data Transfer Object) para transportar a informação
 * sobre as vagas de uma palestra.
 */
public class VagasDto {

    private int totalVagas;
    private long inscritos;
    private long vagasRestantes;

    public VagasDto(int totalVagas, long inscritos, long vagasRestantes) {
        this.totalVagas = totalVagas;
        this.inscritos = inscritos;
        this.vagasRestantes = vagasRestantes;
    }

    // Getters e Setters
    public int getTotalVagas() {
        return totalVagas;
    }

    public void setTotalVagas(int totalVagas) {
        this.totalVagas = totalVagas;
    }

    public long getInscritos() {
        return inscritos;
    }

    public void setInscritos(long inscritos) {
        this.inscritos = inscritos;
    }

    public long getVagasRestantes() {
        return vagasRestantes;
    }

    public void setVagasRestantes(long vagasRestantes) {
        this.vagasRestantes = vagasRestantes;
    }
}