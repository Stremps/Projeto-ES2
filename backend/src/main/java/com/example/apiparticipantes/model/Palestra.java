// Crie este novo arquivo em: src/main/java/com/example/apiparticipantes/model/Palestra.java
package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "palestra")
public class Palestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_palestra")
    private Long id;

    @Column(name = "titulo_palestra", nullable = false)
    private String titulo;

    @Column(name = "data_palestra", nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "local_interno")
    private String localInterno;

    @Column(name = "numero_vagas")
    private Integer numeroVagas;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Participante palestrante; // O participante que dará a palestra

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @OneToMany(mappedBy = "palestra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscricaoPalestra> inscricoes;

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

    public Participante getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(Participante palestrante) {
        this.palestrante = palestrante;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<InscricaoPalestra> getInscricoes() { return inscricoes; }
    public void setInscricoes(List<InscricaoPalestra> inscricoes) { this.inscricoes = inscricoes; }
}