package com.example.apiparticipantes.dto;

public class PalestranteResponseDto {
    private String id;
    private String nome;

    public PalestranteResponseDto(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}