package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.InscricaoPalestra;
import com.example.apiparticipantes.model.Palestra;
import com.example.apiparticipantes.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscricaoPalestraRepository extends JpaRepository<InscricaoPalestra, Long> {

    /**
     * Conta o número de inscrições existentes para uma determinada palestra.
     * @param palestra A palestra para a qual contar as inscrições.
     * @return O número de participantes inscritos.
     */
    long countByPalestra(Palestra palestra);

    /**
     * Verifica se já existe uma inscrição para um participante em uma palestra específica.
     * @param participante O participante a ser verificado.
     * @param palestra A palestra a ser verificada.
     * @return true se a inscrição já existe, false caso contrário.
     */
    boolean existsByParticipanteAndPalestra(Participante participante, Palestra palestra);
}