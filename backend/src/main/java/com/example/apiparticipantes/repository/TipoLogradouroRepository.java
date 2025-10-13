package com.example.apiparticipantes.repository;

import com.example.apiparticipantes.model.TipoLogradouro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoLogradouroRepository extends JpaRepository<TipoLogradouro, String> {
    Optional<TipoLogradouro> findByNomeTipoLogradouro(String nomeTipoLogradouro);
}
