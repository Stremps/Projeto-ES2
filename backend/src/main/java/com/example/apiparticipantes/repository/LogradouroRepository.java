package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Logradouro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogradouroRepository extends JpaRepository<Logradouro, String> {
    Optional<Logradouro> findByNomeLogradouro(String nomeLogradouro);
}
