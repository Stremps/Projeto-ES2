// Crie este novo arquivo em: src/main/java/com/example/apiparticipantes/model/VinculoEvento.java
package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vinculo_evento")
public class VinculoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vinculo")
    private Long id;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_participante")
    private Participante participante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_evento", referencedColumnName = "id_evento")
    private Evento evento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_participacao", referencedColumnName = "id_participacao")
    private TipoParticipacao tipoParticipacao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public TipoParticipacao getTipoParticipacao() {
        return tipoParticipacao;
    }

    public void setTipoParticipacao(TipoParticipacao tipoParticipacao) {
        this.tipoParticipacao = tipoParticipacao;
    }
}