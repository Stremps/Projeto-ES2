// Crie este novo arquivo em: src/main/java/com/example/apiparticipantes/model/InscricaoPalestra.java
package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscricao_palestra")
public class InscricaoPalestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscricao")
    private Long id;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Participante participante;

    @ManyToOne
    @JoinColumn(name = "id_palestra", nullable = false)
    private Palestra palestra;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Palestra getPalestra() {
        return palestra;
    }

    public void setPalestra(Palestra palestra) {
        this.palestra = palestra;
    }
}