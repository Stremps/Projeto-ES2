// Crie este novo arquivo em: src/main/java/com/example/apiparticipantes/model/TipoParticipacao.java
package com.example.apiparticipantes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_participacao")
public class TipoParticipacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacao")
    private Long id;

    @Column(name = "tipo_participacao", nullable = false, unique = true)
    private String nome; // Ex: "Organizador", "Palestrante", "Ouvinte"

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
}