package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.Logradouro;
import com.example.apiparticipantes.model.TipoLogradouro;
import com.example.apiparticipantes.repository.LogradouroRepository;
import org.apache.juli.logging.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logradouros")
@CrossOrigin(origins = "*")
public class LogradouroController {
    private final LogradouroRepository logradouroRepository;

    public LogradouroController(LogradouroRepository logradouroRepository){
        this.logradouroRepository = logradouroRepository;
    }

    @GetMapping
    public List<Logradouro> listar() {
        return logradouroRepository.findAll();
    }

    @GetMapping("/{id}")
    public Logradouro buscar(@PathVariable String id) {
        return logradouroRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Logradouro criar(@RequestBody Logradouro tp) {
        return logradouroRepository.save(tp);
    }

    @PutMapping("/{id}")
    public Logradouro atualizar(@PathVariable String id, @RequestBody Logradouro tp) {
        if (!logradouroRepository.existsById(id)) return null;
        tp.setIdLogradouro(id);
        return logradouroRepository.save(tp);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable String id) {
        logradouroRepository.deleteById(id);
    }
}
