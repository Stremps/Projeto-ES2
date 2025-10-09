package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.Cidade;
import com.example.apiparticipantes.repository.CidadeRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cidades")
@CrossOrigin(origins = "*")
public class CidadeController {

    private final CidadeRepository cidadeRepository;

    public CidadeController(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cidade buscar(@PathVariable String id) {
        return cidadeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Cidade criar(@RequestBody Cidade cidade) {
        return cidadeRepository.save(cidade);
    }

    @PutMapping("/{id}")
    public Cidade atualizar(@PathVariable String id, @RequestBody Cidade cidade) {
        if (!cidadeRepository.existsById(id)) return null;
        cidade.setIdCidade(id);
        return cidadeRepository.save(cidade);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable String id) {
        cidadeRepository.deleteById(id);
    }
}
