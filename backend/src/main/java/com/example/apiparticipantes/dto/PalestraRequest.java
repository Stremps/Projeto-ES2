package com.example.apiparticipantes.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PalestraRequest {

    private String titulo;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String descricao;
    private String localInterno;
    private Integer numeroVagas;
    private Long eventoId; // ID do evento ao qual a palestra pertence
    private String palestranteEmail;


    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalInterno() {
        return localInterno;
    }

    public void setLocalInterno(String localInterno) {
        this.localInterno = localInterno;
    }

    public Integer getNumeroVagas() {
        return numeroVagas;
    }

    public void setNumeroVagas(Integer numeroVagas) {
        this.numeroVagas = numeroVagas;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getPalestranteEmail() {
        return palestranteEmail;
    }

    public void setPalestranteEmail(String palestranteEmail) {
        this.palestranteEmail = palestranteEmail;
    }
}