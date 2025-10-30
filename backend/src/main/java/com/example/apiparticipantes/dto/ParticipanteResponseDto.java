package com.example.apiparticipantes.dto;

import com.example.apiparticipantes.model.Cargo;
import com.example.apiparticipantes.model.Participante;

public class ParticipanteResponseDto {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private Cargo cargo;
    private EnderecoResponseDto endereco;

    public ParticipanteResponseDto(Participante participante) {
        this.id = participante.getIdParticipante();
        this.nome = participante.getNomeParticipante();
        this.email = participante.getEmailParticipante();
        this.telefone = participante.getTelefoneParticipante();
        this.cargo = participante.getCargo();
        // Evita erro se o participante (ex: Admin) não tiver endereço
        if (participante.getEndereco() != null) {
            this.endereco = new EnderecoResponseDto(participante.getEndereco());
        } else {
            this.endereco = null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public EnderecoResponseDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoResponseDto endereco) {
        this.endereco = endereco;
    }
}