package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.TipoLogradouro;
import com.example.apiparticipantes.repository.TipoLogradouroRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipo-logradouros")
@CrossOrigin(origins = "*")
public class TipoLogradouroController {
    private final TipoLogradouroRepository tipoLogradouroRepository;

    public TipoLogradouroController(TipoLogradouroRepository tipoLogradouroRepository){
        this.tipoLogradouroRepository = tipoLogradouroRepository;
    }

    @GetMapping
    public List<TipoLogradouro> listar() {
        return tipoLogradouroRepository.findAll();
    }

    @GetMapping("/{id}")
    public TipoLogradouro buscar(@PathVariable String id) {
        return tipoLogradouroRepository.findById(id).orElse(null);
    }

    @PostMapping
    public TipoLogradouro criar(@RequestBody TipoLogradouro tp) {
        return tipoLogradouroRepository.save(tp);
    }

    @PutMapping("/{id}")
    public TipoLogradouro atualizar(@PathVariable String id, @RequestBody TipoLogradouro tp) {
        if (!tipoLogradouroRepository.existsById(id)) return null;
        tp.setIdTipoLogradouro(id);
        return tipoLogradouroRepository.save(tp);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable String id) {
        tipoLogradouroRepository.deleteById(id);
    }

}
