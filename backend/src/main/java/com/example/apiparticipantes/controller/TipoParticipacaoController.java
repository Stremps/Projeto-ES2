// Crie este novo arquivo em: src/main/java/com/example/apiparticipantes/controller/TipoParticipacaoController.java
package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.TipoParticipacao;
import com.example.apiparticipantes.repository.TipoParticipacaoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-participacao")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAuthority('ADMIN')") // Apenas ADMIN pode gerenciar
public class TipoParticipacaoController {

    private final TipoParticipacaoRepository repository;

    public TipoParticipacaoController(TipoParticipacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TipoParticipacao> listar() {
        return repository.findAll();
    }

    @PostMapping
    public TipoParticipacao criar(@RequestBody TipoParticipacao tipoParticipacao) {
        return repository.save(tipoParticipacao);
    }

    // Você pode adicionar PUT e DELETE se necessário
}