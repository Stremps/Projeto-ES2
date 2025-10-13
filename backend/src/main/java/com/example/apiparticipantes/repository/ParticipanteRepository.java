package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, String> {
    Optional<Participante> findByEmailParticipante(String emailParticipante);
    boolean existsByEmailParticipante(String emailParticipante);
}
