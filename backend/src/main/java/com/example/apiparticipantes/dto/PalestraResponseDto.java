package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.Palestra;
import java.time.LocalDate;
import java.time.LocalTime;

public class PalestraResponseDto {
    private Long id;
    private String titulo;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String localInterno;
    private Integer numeroVagas;
    private PalestranteResponseDto palestrante;
    private EventoSimplesResponseDto evento;

    // Construtor que converte a entidade Palestra para este DTO
    public PalestraResponseDto(Palestra palestra) {
        this.id = palestra.getId();
        this.titulo = palestra.getTitulo();
        this.data = palestra.getData();
        this.horaInicio = palestra.getHoraInicio();
        this.horaFim = palestra.getHoraFim();
        this.localInterno = palestra.getLocalInterno();
        this.numeroVagas = palestra.getNumeroVagas();
        this.palestrante = new PalestranteResponseDto(
                palestra.getPalestrante().getIdParticipante(),
                palestra.getPalestrante().getNomeParticipante()
        );
        this.evento = new EventoSimplesResponseDto(
                palestra.getEvento().getId(),
                palestra.getEvento().getNome()
        );
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public PalestranteResponseDto getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(PalestranteResponseDto palestrante) {
        this.palestrante = palestrante;
    }

    public EventoSimplesResponseDto getEvento() {
        return evento;
    }

    public void setEvento(EventoSimplesResponseDto evento) {
        this.evento = evento;
    }
}