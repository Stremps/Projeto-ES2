package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByCode(String code);
    void deleteByParticipanteIdParticipante(String participanteId); // Para limpar tokens antigos
}