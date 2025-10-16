package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.TipoParticipacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Importar

public interface TipoParticipacaoRepository extends JpaRepository<TipoParticipacao, Long> {
    // Novo método para buscar pelo nome do tipo de participação
    Optional<TipoParticipacao> findByNome(String nome);
}