package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadeRepository extends JpaRepository<Cidade, String> {
    Optional<Cidade> findByNomeCidade(String nomeCidade);
}
