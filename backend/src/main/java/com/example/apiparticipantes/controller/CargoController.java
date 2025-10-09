package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.model.Cargo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargos")
@CrossOrigin(origins = "*") // opcional, caso use frontend separado
public class CargoController {

    @GetMapping
    public Cargo[] listarCargos() {
        // Retorna todas as opções do enum Cargo
        return Cargo.values();
    }
}
