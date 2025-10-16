package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Evento;
import com.example.apiparticipantes.model.Participante;
import com.example.apiparticipantes.model.TipoParticipacao;
import com.example.apiparticipantes.model.VinculoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Importar

public interface VinculoEventoRepository extends JpaRepository<VinculoEvento, Long> {
    Optional<VinculoEvento> findByParticipanteAndEventoAndTipoParticipacao(
            Participante participante, Evento evento, TipoParticipacao tipoParticipacao);


    boolean existsByParticipanteAndEvento(Participante participante, Evento evento);
}