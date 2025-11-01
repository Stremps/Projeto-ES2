package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional; // Importar

public interface VinculoEventoRepository extends JpaRepository<VinculoEvento, Long> {
    Optional<VinculoEvento> findByParticipanteAndEventoAndTipoParticipacao(
            Participante participante, Evento evento, TipoParticipacao tipoParticipacao);

    List<VinculoEvento> findByStatus(StatusInscricao status);

    Optional<VinculoEvento> findByParticipanteIdParticipanteAndEventoIdAndStatus(String idParticipante, Long idEvento, StatusInscricao status);

    Optional<VinculoEvento> findByParticipanteAndEventoAndTipoParticipacaoAndStatus(
            Participante participante,
            Evento evento,
            TipoParticipacao tipo,
            StatusInscricao status
    );

    boolean existsByParticipanteAndEvento(Participante participante, Evento evento);
}