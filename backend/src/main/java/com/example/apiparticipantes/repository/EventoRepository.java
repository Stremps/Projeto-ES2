package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}

