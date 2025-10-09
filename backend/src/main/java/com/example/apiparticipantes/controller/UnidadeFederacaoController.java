package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.UnidadeFederacao;
import com.example.apiparticipantes.repository.UnidadeFederacaoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/unidades-federacao")
@CrossOrigin(origins = "*")
public class UnidadeFederacaoController {

    private final UnidadeFederacaoRepository ufRepository;

    public UnidadeFederacaoController(UnidadeFederacaoRepository ufRepository) {
        this.ufRepository = ufRepository;
    }

    @GetMapping
    public List<UnidadeFederacao> listar() {
        return ufRepository.findAll();
    }

    @GetMapping("/{id}")
    public UnidadeFederacao buscar(@PathVariable String id) {
        return ufRepository.findById(id).orElse(null);
    }

    @PostMapping
    public UnidadeFederacao criar(@RequestBody UnidadeFederacao uf) {
        return ufRepository.save(uf);
    }

    @PutMapping("/{id}")
    public UnidadeFederacao atualizar(@PathVariable String id, @RequestBody UnidadeFederacao uf) {
        if (!ufRepository.existsById(id)) return null;
        uf.setSiglaUf(id);
        return ufRepository.save(uf);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable String id) {
        ufRepository.deleteById(id);
    }
}
