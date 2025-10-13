package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.Endereco;
import com.example.apiparticipantes.repository.EnderecoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
@CrossOrigin(origins = "*")
public class EnderecoController {

    private final EnderecoRepository enderecoRepository;

    public EnderecoController(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @GetMapping
    public List<Endereco> listar() {
        return enderecoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Endereco buscar(@PathVariable Long id) {
        return enderecoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Endereco criar(@RequestBody Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    @PutMapping("/{id}")
    public Endereco atualizar(@PathVariable Long id, @RequestBody Endereco endereco) {
        if (!enderecoRepository.existsById(id)) return null;
        endereco.setIdEndereco(id);
        return enderecoRepository.save(endereco);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        enderecoRepository.deleteById(id);
    }
}
