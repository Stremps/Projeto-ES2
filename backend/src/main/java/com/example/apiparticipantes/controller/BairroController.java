package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.Bairro;
import com.example.apiparticipantes.repository.BairroRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bairros")
@CrossOrigin(origins = "*")
public class BairroController {

    private final BairroRepository bairroRepository;

    public BairroController(BairroRepository bairroRepository) {
        this.bairroRepository = bairroRepository;
    }

    @GetMapping
    public List<Bairro> listar() {
        return bairroRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bairro buscar(@PathVariable String id) {
        return bairroRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Bairro criar(@RequestBody Bairro bairro) {
        return bairroRepository.save(bairro);
    }

    @PutMapping("/{id}")
    public Bairro atualizar(@PathVariable String id, @RequestBody Bairro bairro) {
        if (!bairroRepository.existsById(id)) return null;
        bairro.setIdBairro(id);
        return bairroRepository.save(bairro);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable String id) {
        bairroRepository.deleteById(id);
    }
}
