package com.example.apiparticipantes.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SessionController {

    @GetMapping("/api/session")
    public Map<String, Object> session(Authentication authentication) {
        // Authentication vem do SecurityContext (populado pelo filtro JWT)
        String username = authentication.getName();
        return Map.of("message", "Sessão ativa", "usuario", username);
    }
}
