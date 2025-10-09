package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, String> {
    Optional<Bairro> findByNomeBairro(String nomeBairro);
}
