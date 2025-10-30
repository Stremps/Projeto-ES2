package com.example.apiparticipantes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_code") // Nome da tabela
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10) // Nome 'code', tamanho 10
    private String code;

    @OneToOne(targetEntity = Participante.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id_participante", unique = true) // Adicionado unique=true aqui também
    private Participante participante;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Construtor, Getters e Setters para 'code' (NÃO 'token')
    public PasswordResetToken() {}

    public PasswordResetToken(String code, Participante participante, LocalDateTime expiryDate) {
        this.code = code;
        this.participante = participante;
        this.expiryDate = expiryDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Participante getParticipante() { return participante; }
    public void setParticipante(Participante participante) { this.participante = participante; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public boolean isExpired() { return LocalDateTime.now().isAfter(this.expiryDate); }
}