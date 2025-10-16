package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.Evento;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoResponseDto {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalTime horaInicio;
    private String descricao;
    private PalestranteResponseDto criador;
    private EnderecoResponseDto endereco;

    public EventoResponseDto(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.dataInicio = evento.getDataInicio();
        this.horaInicio = evento.getHoraInicio();
        this.descricao = evento.getDescricao();
        this.criador = new PalestranteResponseDto(
                evento.getCriador().getIdParticipante(),
                evento.getCriador().getNomeParticipante()
        );
        this.endereco = new EnderecoResponseDto(evento.getEndereco());
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PalestranteResponseDto getCriador() {
        return criador;
    }

    public void setCriador(PalestranteResponseDto criador) {
        this.criador = criador;
    }

    public EnderecoResponseDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoResponseDto endereco) {
        this.endereco = endereco;
    }
}