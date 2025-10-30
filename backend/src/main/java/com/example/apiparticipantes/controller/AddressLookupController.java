package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.ViaCepResponseDto;
import com.example.apiparticipantes.service.ViaCepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/address")

public class AddressLookupController {

    private final ViaCepService viaCepService;

    public AddressLookupController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/lookup/{cep}")
    public ResponseEntity<ViaCepResponseDto> lookupAddressByCep(@PathVariable String cep) {
        // A validação básica do formato é feita no ViaCepService
        ViaCepResponseDto address = viaCepService.getAddressByCep(cep)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado ou inválido."));

        return ResponseEntity.ok(address);
    }
}